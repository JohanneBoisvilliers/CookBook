package com.example.cookbook.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class IngredientData (

        @PrimaryKey(autoGenerate = true)
        val ingredientDetailsId:Long=0L,
        val recipeId:Long,
        val ingredientDatabaseId:Long,
        val unit: String,
        val quantity: Int
)