package com.example.cookbook.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BaseDataRecipe (
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0L,
        val name: String? = "",
        val numberOfLike:Int = 0,
        val isAlreadyDone: Boolean? = false,
        val addDate: String? = ""
)
