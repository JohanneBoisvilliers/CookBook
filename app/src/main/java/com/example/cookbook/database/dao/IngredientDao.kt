package com.example.cookbook.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.cookbook.models.IngredientDatabase

@Dao
interface IngredientDao {
    @Query("SELECT * FROM IngredientDatabase WHERE ingredientDatabaseId= :id")
    fun getIngredientDatabase(id:Long):LiveData<IngredientDatabase>

    @Query("SELECT name FROM IngredientDatabase")
    suspend fun getIngredientList():List<String>
}