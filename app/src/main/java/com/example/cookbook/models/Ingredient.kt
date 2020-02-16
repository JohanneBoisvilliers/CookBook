package com.example.cookbook.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingredient (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val quantity:Int= 0,
    val unityOfMeasure:String?,
    val name: String? = null
)
