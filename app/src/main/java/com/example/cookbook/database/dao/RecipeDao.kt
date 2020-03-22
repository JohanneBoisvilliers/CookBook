package com.example.cookbook.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cookbook.models.BaseDataRecipe
import com.example.cookbook.models.Ingredient
import com.example.cookbook.models.Recipe


@Dao
interface RecipeDao {
    @Query("SELECT * FROM BaseDataRecipe")
    @Transaction
    fun getRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM BaseDataRecipe WHERE baseRecipeId= :recipeId")
    @Transaction
    fun getSpecificRecipe(recipeId : Long): LiveData<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: BaseDataRecipe): Long
}