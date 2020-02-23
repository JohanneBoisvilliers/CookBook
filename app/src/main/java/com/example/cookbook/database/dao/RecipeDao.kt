package com.example.cookbook.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.cookbook.models.Recipe

@Dao
interface RecipeDao {
    @Query("SELECT * FROM BaseDataRecipe")
    @Transaction
    fun getRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM BaseDataRecipe WHERE id= :recipeId")
    @Transaction
    fun getSpecificRecipe(recipeId : Long): LiveData<Recipe>
}