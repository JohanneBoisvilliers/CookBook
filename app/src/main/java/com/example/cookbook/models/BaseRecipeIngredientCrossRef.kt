package com.example.cookbook.models

import androidx.room.Entity

@Entity(primaryKeys = ["baseRecipeId","ingredientId"])
data class BaseRecipeIngredientCrossRef(
        val baseRecipeId : Long,
        val ingredientId : Long
)