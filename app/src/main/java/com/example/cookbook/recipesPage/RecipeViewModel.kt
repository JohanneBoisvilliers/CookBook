package com.example.cookbook.recipesPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookbook.database.dao.IngredientDao
import com.example.cookbook.models.BaseDataRecipe
import com.example.cookbook.models.IngredientDatabase
import com.example.cookbook.models.Recipe
import com.example.cookbook.repositories.IngredientDataRepository
import com.example.cookbook.repositories.RecipesDataRepository
import kotlinx.coroutines.runBlocking

class RecipeViewModel(private val mRecipesDataRepository: RecipesDataRepository,
                      private val mIngredientDataRepository:IngredientDataRepository) : ViewModel() {

    val recipes: LiveData<List<Recipe>> = mRecipesDataRepository.recipes
    val isUpdateModeOn = MutableLiveData<Boolean>(false)
    val actualRecipe = MutableLiveData<Recipe>()

    fun getSpecificRecipe(recipeId: Long): LiveData<Recipe> {
        return mRecipesDataRepository.getSpecificRecipe(recipeId)
    }

    fun getIngredientDatabase(ingredientDatabaseId:Long):LiveData<IngredientDatabase>{
        return mIngredientDataRepository.getIngredientDatabase(ingredientDatabaseId)
    }

}