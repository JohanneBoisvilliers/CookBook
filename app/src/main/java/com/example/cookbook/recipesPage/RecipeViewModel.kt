package com.example.cookbook.recipesPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.cookbook.database.dao.IngredientDao
import com.example.cookbook.models.*
import com.example.cookbook.repositories.IngredientDataRepository
import com.example.cookbook.repositories.RecipesDataRepository
import kotlinx.coroutines.runBlocking

class RecipeViewModel(private val mRecipesDataRepository: RecipesDataRepository,
                      private val mIngredientDataRepository:IngredientDataRepository) : ViewModel() {

    val recipes: LiveData<List<Recipe>> = mRecipesDataRepository.recipes
    val isUpdateModeOn = MutableLiveData(false)
    val actualRecipe = MutableLiveData<Recipe>()
    val ingredientList = MutableLiveData<MutableList<Ingredient>>()
    val stepList = MutableLiveData<MutableList<Step>>()
    val quantity = MutableLiveData<Int>()
    val unit = MutableLiveData<String>()
    val ingredientName = MutableLiveData<String>()
    val newStepText = MutableLiveData<String>()

    val ingredientListName: LiveData<List<String>> = liveData {
        val data = mIngredientDataRepository.getIngredientList()
        emit(data)
    }

    fun getSpecificRecipe(recipeId: Long): LiveData<Recipe> {
        return mRecipesDataRepository.getSpecificRecipe(recipeId)
    }

    fun getIngredientDatabase(ingredientDatabaseId:Long):LiveData<IngredientDatabase>{
        return mIngredientDataRepository.getIngredientDatabase(ingredientDatabaseId)
    }

}