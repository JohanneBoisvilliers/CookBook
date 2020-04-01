package com.example.cookbook.repositories

import androidx.lifecycle.LiveData
import com.example.cookbook.database.dao.IngredientDao
import com.example.cookbook.models.IngredientData
import com.example.cookbook.models.IngredientDatabase

class IngredientDataRepository(val ingredientDao: IngredientDao) {
    suspend fun getIngredientDatabase(id:Long):IngredientDatabase{
        return ingredientDao.getIngredientDatabase(id)
    }

    suspend fun getIngredientList():List<IngredientDatabase>{
        return ingredientDao.getIngredientList()
    }

    suspend fun updateIngredients(vararg ingredients:IngredientData){
         ingredientDao.updateIngredient(*ingredients)
    }

    suspend fun deleteIngredient(vararg ingredients:IngredientData){
        ingredientDao.deleteIngredient(*ingredients)
    }
}