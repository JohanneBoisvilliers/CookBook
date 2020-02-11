package com.example.cookbook.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation


data class Recipe (
        @Embedded
        var baseDataRecipe: BaseDataRecipe?,
        @Relation(parentColumn = "id",entityColumn = "recipeId")
        var photoList:MutableList<Photo>
){
    constructor() : this(null, mutableListOf())
}