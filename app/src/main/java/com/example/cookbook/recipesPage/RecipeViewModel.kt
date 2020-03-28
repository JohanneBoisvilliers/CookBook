package com.example.cookbook.recipesPage

import androidx.lifecycle.*
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
    val actualRecipe = MutableLiveData<Recipe>()
    val ingredientList = MutableLiveData<MutableList<Ingredient>>()
    val photoList = MutableLiveData<MutableList<Photo>>()
    val stepList = MutableLiveData<MutableList<Step>>()
    val quantity = MutableLiveData<Int>()
    val unit = MutableLiveData<String>()
    val newStepText = MutableLiveData<String>()
    val ingredientPicked = MutableLiveData<IngredientDatabase>()
    val ingredientDatabaseList = ConcurrentLinkedQueue<IngredientDatabase>()

    val ingredientListPicked: LiveData<List<IngredientDatabase>> = liveData {
        val data = withContext(Dispatchers.IO) { mIngredientDataRepository.getIngredientList() }
        emit(data)
    }

    private suspend fun getSpecificRecipe(recipeId: Long): Recipe {
        return mRecipesDataRepository.getSpecificRecipe(recipeId)
    }

    private suspend fun getIngredientDatabase(ingredientDatabaseId: Long): IngredientDatabase {
        return mIngredientDataRepository.getIngredientDatabase(ingredientDatabaseId)
    }

    fun getRecipeWithIngredient(recipeId: Long) = liveData {
        val tempIngredientList = mutableListOf<Ingredient>()
        // get the recipe
        val recipe = withContext(Dispatchers.Default) { getSpecificRecipe(recipeId) }
        // for each item in recipe ingredient list, get the ingredient database
        withContext(Dispatchers.Default) {
            recipe.ingredientList.forEach {
                launch(Dispatchers.IO) {
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

    fun updateIngredients(ingredients: IngredientDatabase) {
        return mIngredientDataRepository.updateIngredients(ingredients)
    }

}