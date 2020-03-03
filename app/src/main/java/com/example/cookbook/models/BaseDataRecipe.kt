package com.example.cookbook.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BaseDataRecipe (
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,
        var name: String? = "",
        var numberOfLike:Int = 0,
        var isAlreadyDone: Boolean? = false,
        var addDate: String? = ""
)
