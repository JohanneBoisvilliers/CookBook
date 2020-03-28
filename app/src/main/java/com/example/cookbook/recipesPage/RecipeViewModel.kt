package com.example.cookbook.recipesPage

import androidx.lifecycle.*
import com.example.cookbook.models.*
import com.example.cookbook.repositories.IngredientDataRepository
import com.example.cookbook.repositories.RecipesDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    val ingredientName = MutableLiveData<String>()
    val newStepText = MutableLiveData<String>()
    val ingredientDatabaseList = ConcurrentLinkedQueue<IngredientDatabase>()


    val ingredientListName: LiveData<List<String>> = liveData {
        val data = mIngredientDataRepository.getIngredientList()
        emit(data)
    }

    suspend fun getSpecificRecipe(recipeId: Long): Recipe {
        return mRecipesDataRepository.getSpecificRecipe(recipeId)
    }

    suspend fun getIngredientDatabase(ingredientDatabaseId: Long): IngredientDatabase {
        return mIngredientDataRepository.getIngredientDatabase(ingredientDatabaseId)
    }

    fun getRecipeWithIngredient(recipeId: Long) = liveData {
        val recipe = withContext(Dispatchers.Default) { getSpecificRecipe(recipeId) }
        withContext(Dispatchers.Default) {
            recipe.ingredientList.forEach {
                launch(Dispatchers.IO) {
                    ingredientDatabaseList.add(getIngredientDatabase(it.ingredientDatabaseId))
                }
            }
        }
        emit(recipe)
    }

    fun updateRecipe(recipe: BaseDataRecipe) {
        return mRecipesDataRepository.updateRecipe(recipe)
    }

    fun updateIngredients(ingredients: IngredientDatabase) {
        return mIngredientDataRepository.updateIngredients(ingredients)
    }

}