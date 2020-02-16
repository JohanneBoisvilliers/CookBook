package com.example.cookbook.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Ingredient {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name: String? = null
}
