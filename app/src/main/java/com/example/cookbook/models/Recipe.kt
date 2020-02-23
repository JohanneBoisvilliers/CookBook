package com.example.cookbook.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation


data class Recipe (
        @Embedded
        var baseDataRecipe: BaseDataRecipe? = null,
        @Relation(parentColumn = "id",entityColumn = "recipeId")
        var photoList:MutableList<Photo>

){
        constructor() : this(null, mutableListOf())
}

