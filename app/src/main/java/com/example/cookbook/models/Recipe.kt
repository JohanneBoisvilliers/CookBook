package com.example.cookbook.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation


data class Recipe (
        @Embedded
        var baseDataRecipe: BaseDataRecipe? = null,
        @Relation(parentColumn = "baseRecipeId",entityColumn = "recipeId")
        var photoList:MutableList<Photo>,
        @Relation(parentColumn = "baseRecipeId",
                entity = Ingredient::class,
                entityColumn = "ingredientId",
                associateBy = Junction(
                        value = BaseRecipeIngredientCrossRef::class,
                        parentColumn = "baseRecipeId",
                        entityColumn = "ingredientId"
                        ))
        var ingredientList:MutableList<Ingredient>,
        @Relation(parentColumn="baseRecipeId",entityColumn = "recipeId")
        var stepList:MutableList<Step>
){
        constructor() : this(null, mutableListOf(), mutableListOf(), mutableListOf())
}

