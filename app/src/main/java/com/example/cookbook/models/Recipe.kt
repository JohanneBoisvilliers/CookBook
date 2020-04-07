package com.example.cookbook.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation


class Recipe (
        @Embedded
        var baseDataRecipe: BaseDataRecipe?,
        @Relation(parentColumn = "baseRecipeId",entityColumn = "recipeId")
        var photoList:MutableList<Photo>,
        @Relation(parentColumn = "baseRecipeId",
                entityColumn = "recipeId")
        var ingredientList:MutableList<IngredientData>,
        @Relation(parentColumn="baseRecipeId",entityColumn = "recipeId")
        var stepList:MutableList<Step>
){
        constructor() : this(null, mutableListOf(), mutableListOf(), mutableListOf())
}

