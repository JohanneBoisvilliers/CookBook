package com.example.cookbook.repositories

import androidx.lifecycle.LiveData
import com.example.cookbook.database.dao.RecipeDao
import com.example.cookbook.models.BaseDataRecipe
import com.example.cookbook.models.Recipe

class RecipesDataRepository(private val mRecipeDao: RecipeDao) {
    val recipes: LiveData<List<Recipe>>
        get() = mRecipeDao.getRecipes()

    suspend fun getSpecificRecipe(recipeId: Long): Recipe {
        return mRecipeDao.getSpecificRecipe(recipeId)
    }

    suspend fun insertRecipe(recipe: BaseDataRecipe): Long {
        return mRecipeDao.insertRecipe(recipe)
    }

    fun updateRecipe(recipe:BaseDataRecipe){
        return mRecipeDao.updateRecipe(recipe)
    }

}