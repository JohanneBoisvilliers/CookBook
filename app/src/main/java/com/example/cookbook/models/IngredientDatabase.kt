package com.example.cookbook.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class IngredientDatabase (

        @PrimaryKey(autoGenerate = true)
        var ingredientDatabaseId: Long = 0,
        var name: String
){
        constructor():this(0,"")
}
