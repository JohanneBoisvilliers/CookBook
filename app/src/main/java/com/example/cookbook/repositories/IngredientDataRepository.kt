package com.example.cookbook.repositories

import androidx.lifecycle.LiveData
import com.example.cookbook.database.dao.IngredientDao
import com.example.cookbook.models.IngredientDatabase

class IngredientDataRepository(val ingredientDao: IngredientDao) {
    fun getIngredientDatabase(id:Long):LiveData<IngredientDatabase>{
        return ingredientDao.getIngredientDatabase(id)
    }

    suspend fun getIngredientList():List<String>{
        return ingredientDao.getIngredientList()
    }
}