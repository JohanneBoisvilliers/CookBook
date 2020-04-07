package com.example.cookbook.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Step(
        @PrimaryKey(autoGenerate = true)
        val id: Long=0L,
        val recipeId:Long,
        val photoUrl: String="",
        val description: String
)