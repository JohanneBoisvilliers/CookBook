package com.example.cookbook.models

import android.os.Parcel
import android.os.Parcelable
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
