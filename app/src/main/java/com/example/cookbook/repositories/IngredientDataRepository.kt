package com.example.cookbook.repositories

import androidx.lifecycle.LiveData
import com.example.cookbook.database.dao.IngredientDao
import com.example.cookbook.models.IngredientDatabase

class IngredientDataRepository(val ingredientDao: IngredientDao) {
    suspend fun getIngredientDatabase(id:Long):IngredientDatabase{
        return ingredientDao.getIngredientDatabase(id)
    }

    suspend fun getIngredientList():List<IngredientDatabase>{
        return ingredientDao.getIngredientList()
    }

    fun updateIngredients(ingredients:IngredientDatabase){
        return ingredientDao.updateIngredient(ingredients)
    }
}