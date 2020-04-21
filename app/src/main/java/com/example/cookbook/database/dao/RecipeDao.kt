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

    @Query("SELECT * FROM BaseDataRecipe ORDER BY RANDOM() LIMIT 10")
    @Transaction
    fun getRandomRecipes(): List<Recipe>

    @Query("SELECT * FROM BaseDataRecipe WHERE baseRecipeId= :recipeId")
    @Transaction
    suspend fun getSpecificRecipe(recipeId : Long): Recipe

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: BaseDataRecipe): Long

    @Update
    fun updateRecipe(recipe:BaseDataRecipe)

    @Query("UPDATE BaseDataRecipe SET name = :name WHERE baseRecipeId= :recipeId")
    suspend fun updateRecipeName(recipeId:Long,name : String)

    @Query("UPDATE BaseDataRecipe SET recipeUrl = :recipeUrl WHERE baseRecipeId = :recipeId")
    suspend fun updateRecipeUrl(recipeId: Long,recipeUrl:String)
}