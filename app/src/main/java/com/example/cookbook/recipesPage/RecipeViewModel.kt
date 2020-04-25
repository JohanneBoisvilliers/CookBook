package com.example.cookbook.recipesPage

import androidx.lifecycle.*
import com.example.cookbook.addRecipePage.plusAssign
import com.example.cookbook.models.*
import com.example.cookbook.repositories.*
import com.example.cookbook.utils.RequestResult
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import java.io.File
import java.util.concurrent.CancellationException
import java.util.concurrent.ConcurrentLinkedQueue

class RecipeViewModel(private val mRecipesDataRepository: RecipesDataRepository,
                      private val mIngredientDataRepository: IngredientDataRepository,
                      private val mStepDataRepository: StepDataRepository,
                      private val mPhotoDataRepository: PhotoDataRepository,
                      private val mFirestoreRecipeRepository: FirestoreRecipeRepository) : ViewModel() {

    val recipes: LiveData<List<Recipe>> = mRecipesDataRepository.recipes
    val isUpdateModeOn = MutableLiveData(false)
    val isUpdateIconPressed = MutableLiveData(false)
    val actualRecipe = MutableLiveData<Recipe>()
    val ingredientList = MutableLiveData<MutableList<Ingredient>>()
    val photoList = MutableLiveData<MutableList<Photo>>()
    val stepList = MutableLiveData<MutableList<Step>>()
    val compressedPhotoList = MutableLiveData<MutableList<File>>()
    val quantity = MutableLiveData<Int>()
    val unit = MutableLiveData<String>()
    val shareDescription = MutableLiveData("")
    val newStepText = MutableLiveData<String>()
    val ingredientPicked = MutableLiveData<IngredientDatabase>()
    val ingredientDatabaseList = ConcurrentLinkedQueue<IngredientDatabase>()
    val photoSelected = MutableLiveData(0)
    private val uriList: MutableList<String> = mutableListOf()
    val isNotOnline: Boolean
        get() {
            return actualRecipe.value?.baseDataRecipe?.recipeUrl.isNullOrEmpty()
        }
    val isReadOnly = MutableLiveData(false)
    val sharedRecipesList = MutableLiveData<MutableList<Map<String, Any>>>()
    val favoritesRecipesList = MutableLiveData<MutableList<Map<String, Any>>>()


    //----------------- RECIPES -----------------

    fun getSharedRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            sharedRecipesList.postValue(mFirestoreRecipeRepository.getRecipes("sharedRecipes"))
        }
    }

    fun getFavoritesRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesRecipesList.postValue(mFirestoreRecipeRepository.getRecipes("favorites"))
        }
    }

    fun observerOnSharedRecipe() {
        FirebaseFirestore.getInstance().collection("sharedRecipes").addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null) {
                getSharedRecipes()
            } else {
            }
        }
    }

    // fetch a recipe depending on an id (when user click on a recipe in recipe list)
    private suspend fun getSpecificRecipe(recipeId: Long): Recipe {
        return mRecipesDataRepository.getSpecificRecipe(recipeId)
    }

    // merge both function getSpecificRecipe and getIngredientDatabase for setting all recipe field
    fun getRecipeWithIngredient(recipeId: Long) = liveData {
        val tempIngredientList = mutableListOf<Ingredient>()
        // get the recipe
        val recipe = withContext(Dispatchers.IO) { getSpecificRecipe(recipeId) }
        // for each item in recipe ingredient list, get the ingredient database
        withContext(Dispatchers.IO) {
            recipe.ingredientList.forEach {
                launch {
                    fetchIngredientList(it, tempIngredientList)
                }
            }
        }
        // post the new ingredient list for update UI
        ingredientList.postValue(tempIngredientList)
        // post the new step list for update UI
        stepList.postValue(recipe.stepList)
        // post the new photo list for update UI
        photoList.postValue(recipe.photoList)
        emit(recipe)
    }

    val randomRecipes: LiveData<List<Recipe>> = liveData {
        withContext(Dispatchers.IO) {
            val recipes = mRecipesDataRepository.getRandomRecipe()
            emit(recipes)
        }
    }

    fun updateRecipeName(recipeId: Long, name: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mRecipesDataRepository.updateRecipeName(recipeId, name)
            }
        }
    }

    fun updateRecipeUrl(recipeId: Long, recipeUrl: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mRecipesDataRepository.updateRecipeUrl(recipeId, recipeUrl)
            }
        }
    }

    fun sharedRecipe(recipe: Recipe, description: String, photoUrls: List<String>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mFirestoreRecipeRepository.sharedRecipe(recipe, description, photoUrls, ingredientList.value!!)
                        .addOnSuccessListener {
                            println("Shared recipe success")
                        }
                        .addOnFailureListener {
                            println("Error in : recipeViewModel => shareRecipe()")
                        }
            }
        }
    }

    fun likeRecipe(counterDocRef: DocumentReference, recipeDocRef: DocumentReference, numShards: Int, isChecked: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mFirestoreRecipeRepository.likeRecipe(counterDocRef, recipeDocRef, numShards, isChecked)
                        .addOnSuccessListener { println("increment succeess") }
                        .addOnFailureListener { println("increment failed") }
            }
        }
    }

    //----------------- INGREDIENTS -----------------

    // fetch ingredient list for autocomplete text view
    val ingredientListPicked: LiveData<List<IngredientDatabase>> = liveData {
        val data = withContext(Dispatchers.IO) { mIngredientDataRepository.getIngredientList() }
        emit(data)
    }

    // fetch ingredient database for setting name of ingredient in ingredient list in recipe detail page
    private suspend fun getIngredientDatabase(ingredientDatabaseId: Long): IngredientDatabase {
        return mIngredientDataRepository.getIngredientDatabase(ingredientDatabaseId)
    }

    // update ingredient (for ingredient bottom sheet)
    fun updateIngredients(vararg ingredients: IngredientData) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { mIngredientDataRepository.updateIngredients(*ingredients) }
        }
    }

    // delete ingredient (for ingredient bottom sheet)
    fun deleteIngredients(vararg ingredients: IngredientData) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { mIngredientDataRepository.deleteIngredient(*ingredients) }
        }
    }

    fun addIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            val id = withContext(Dispatchers.IO) { mIngredientDataRepository.addIngredient(ingredient.ingredientData) }
            ingredient.ingredientData = ingredient.ingredientData.copy(ingredientDetailsId = id)

            ingredientDatabaseList.add(ingredient.ingredientDatabase)
            ingredientList.plusAssign(ingredient)
        }
    }

    //----------------- STEPS -----------------

    // add a step into database and add it into stepList at the same time
    fun addStep(step: Step) {
        viewModelScope.launch {
            val id = withContext(Dispatchers.IO) { mStepDataRepository.addStep(step) }
            val stepToAdd = step.copy(id = id)

            stepList.plusAssign(stepToAdd)
        }
    }

    // update a step into database
    fun updateStep(step: Step) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mStepDataRepository.updateStep(step)
            }
        }
    }

    fun deleteStep(step: Step) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mStepDataRepository.deleteStep(step)
            }
        }
    }

    //----------------- PHOTOS -----------------

    fun insertPhoto(vararg photo: Photo) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mPhotoDataRepository.insertPhoto(*photo)
            }
        }
    }

    fun deletePhoto(photo: Photo) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mPhotoDataRepository.deletePhoto(photo)
            }
            photoList.value!!.removeAt(photoSelected.value!!)
            photoList.postValue(photoList.value)
        }
    }

    val uploadPhoto = liveData<RequestResult<Any>> {
        emit(RequestResult.Loading("chargement"))

        try {


            viewModelScope.launch(Dispatchers.IO) {

                delay(500L)
//                withContext(Dispatchers.IO) {
//                    compressedPhotoList.value!!.forEach {
//                        launch {
//                            uriList.add(mFirestoreRecipeRepository.uploadPhoto(actualRecipe.value!!, it).toString())
//                        }
//                    }
//                }
//                withContext(Dispatchers.IO) {
//                    sharedRecipe(
//                            recipe = actualRecipe.value!!,
//                            description = shareDescription.value!!,
//                            photoUrls = uriList
//                    )
//                }
            }
            emit(RequestResult.Success(true))
        } catch (e: Exception) {
            println(e)
        }
    }
    //----------------- ARTICLE -----------------

    val article: LiveData<HeadLineArticle> = liveData {
        val article = withContext(Dispatchers.IO) { mFirestoreRecipeRepository.getHeadLineArticle() }
        emit(article)
    }

    //----------------- PRIVATE METHODS -----------------

    private suspend fun fetchIngredientList(ingredientData: IngredientData, tempIngredientList: MutableList<Ingredient>) {
        // create a new ingredient with ingredientData + ingredientDatabase
        val ingredient = Ingredient(ingredientData, getIngredientDatabase(ingredientData.ingredientDatabaseId))
        // save ingredient database fetch into a list
        ingredientDatabaseList.add(ingredient.ingredientDatabase)
        // save ingredient into a list
        tempIngredientList.add(ingredient)
    }
}