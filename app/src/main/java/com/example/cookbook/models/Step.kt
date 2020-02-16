package com.example.cookbook.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Step(
        @PrimaryKey(autoGenerate = true)
        val id:Long,
        val recipeId:Long,
        val title:String,
        val description:String?,
        val photoUrl:String?
)