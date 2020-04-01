package com.example.cookbook.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.cookbook.models.IngredientData
import com.example.cookbook.models.IngredientDatabase

@Dao
interface IngredientDao {
    @Query("SELECT * FROM IngredientDatabase WHERE ingredientDatabaseId= :id")
    suspend fun getIngredientDatabase(id:Long):IngredientDatabase

    @Query("SELECT * FROM IngredientDatabase")
    suspend fun getIngredientList():List<IngredientDatabase>

    @Update
    suspend fun updateIngredient(vararg ingredients:IngredientData)
}