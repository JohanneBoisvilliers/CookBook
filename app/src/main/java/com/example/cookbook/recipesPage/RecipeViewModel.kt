package com.example.cookbook.recipesPage

import androidx.lifecycle.*
import com.example.cookbook.addRecipePage.plusAssign
import com.example.cookbook.models.*
import com.example.cookbook.repositories.IngredientDataRepository
import com.example.cookbook.repositories.PhotoDataRepository
import com.example.cookbook.repositories.RecipesDataRepository
import com.example.cookbook.repositories.StepDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentLinkedQueue

class RecipeViewModel(private val mRecipesDataRepository: RecipesDataRepository,
                      private val mIngredientDataRepository: IngredientDataRepository,
                      private val mStepDataRepository: StepDataRepository,
                      private val mPhotoDataRepository: PhotoDataRepository) : ViewModel() {

    val recipes: LiveData<List<Recipe>> = mRecipesDataRepository.recipes
    val isUpdateModeOn = MutableLiveData(false)
    val isUpdateIconPressed = MutableLiveData(false)
    val actualRecipe = MutableLiveData<Recipe>()
    val ingredientList = MutableLiveData<MutableList<Ingredient>>()
    val photoList = MutableLiveData<MutableList<Photo>>()
    val stepList = MutableLiveData<MutableList<Step>>()
    val quantity = MutableLiveData<Int>()
    val unit = MutableLiveData<String>()
    val newStepText = MutableLiveData<String>()
    val ingredientPicked = MutableLiveData<IngredientDatabase>()
    val ingredientDatabaseList = ConcurrentLinkedQueue<IngredientDatabase>()

    //----------------- ASYNC RECIPES -----------------

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

    fun updateRecipeName(recipeId: Long, name: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mRecipesDataRepository.updateRecipeName(recipeId, name)
            }
        }
    }

    //----------------- ASYNC INGREDIENTS -----------------

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

    //----------------- ASYNC STEPS -----------------

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

    //----------------- ASYNC PHOTOS -----------------

    fun insertPhoto(vararg photo:Photo){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                mPhotoDataRepository.insertPhoto(*photo)
            }
        }
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