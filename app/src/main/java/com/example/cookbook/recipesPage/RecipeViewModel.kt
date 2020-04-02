package com.example.cookbook.recipesPage

import androidx.lifecycle.*
import com.example.cookbook.addRecipePage.plusAssign
import com.example.cookbook.models.*
import com.example.cookbook.repositories.IngredientDataRepository
import com.example.cookbook.repositories.RecipesDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentLinkedQueue

class RecipeViewModel(private val mRecipesDataRepository: RecipesDataRepository,
                      private val mIngredientDataRepository: IngredientDataRepository) : ViewModel() {

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

    // fetch ingredient list for autocomplete text view
    val ingredientListPicked: LiveData<List<IngredientDatabase>> = liveData {
        val data = withContext(Dispatchers.IO) { mIngredientDataRepository.getIngredientList() }
        emit(data)
    }

    // fetch a recipe depending on an id (when user click on a recipe in recipe list)
    private suspend fun getSpecificRecipe(recipeId: Long): Recipe {
        return mRecipesDataRepository.getSpecificRecipe(recipeId)
    }

    // fetch ingredient database for setting name of ingredient in ingredient list in recipe detail page
    private suspend fun getIngredientDatabase(ingredientDatabaseId: Long): IngredientDatabase {
        return mIngredientDataRepository.getIngredientDatabase(ingredientDatabaseId)
    }

    // merge both function getSpecificRecipe and getIngredientDatabase for setting all recipe field
    fun getRecipeWithIngredient(recipeId: Long) = liveData {
        val tempIngredientList = mutableListOf<Ingredient>()
        // get the recipe
        val recipe = withContext(Dispatchers.IO) { getSpecificRecipe(recipeId) }
        // for each item in recipe ingredient list, get the ingredient database
        withContext(Dispatchers.IO) {
            recipe.ingredientList.forEach {
                launch{
                    // create a new ingredient with it(ingredientData) + ingredientDatabase
                    val ingredient = Ingredient(it, getIngredientDatabase(it.ingredientDatabaseId))
                    // save ingredient database fetch into a list
                    ingredientDatabaseList.add(ingredient.ingredientDatabase)
                    // save ingredient into a list
                    tempIngredientList.add(ingredient)
                }
            }
        }
        // post the new ingredient list for update UI
        ingredientList.postValue(tempIngredientList)
        emit(recipe)
    }

    fun updateRecipe(recipe: BaseDataRecipe) {
        return mRecipesDataRepository.updateRecipe(recipe)
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
            ingredientDatabaseList.add(ingredient.ingredientDatabase)
            ingredientList.plusAssign(ingredient)
        }

    }


}