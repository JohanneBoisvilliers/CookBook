package com.example.cookbook.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photo (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val recipeId: Long,
    val photoUrl: String
)


