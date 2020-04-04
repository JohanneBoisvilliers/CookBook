package com.example.cookbook.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.cookbook.models.Photo

@Dao
interface PhotoDao {
    @Insert
    suspend fun insertPhoto(vararg photo: Photo)
}