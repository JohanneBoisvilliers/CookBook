package com.example.cookbook.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingredient (

        @PrimaryKey(autoGenerate = true)
        val ingredientId: Long = 0,
        val name: String? = null
)
