package com.example.cookbook.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation


data class Recipe (
        @Embedded
        val baseDataRecipe: BaseDataRecipe?,
        @Relation(parentColumn = "id",entityColumn = "recipeId")
        val photoList:MutableList<Photo>,
        @Relation(parentColumn ="id",entityColumn = "recipeId")
        val stepList:MutableList<Step>,
        @Relation(parentColumn = "id",entityColumn = "recipeId")
        val ingredientList:MutableList<Ingredient>
){
    constructor() : this(null, mutableListOf(), mutableListOf(), mutableListOf())
}