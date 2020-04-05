package com.example.cookbook.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.example.cookbook.models.Photo

@Dao
interface PhotoDao {
    @Insert
    suspend fun insertPhoto(vararg photo: Photo)

    @Delete
    suspend fun deletePhoto(photo:Photo)
}