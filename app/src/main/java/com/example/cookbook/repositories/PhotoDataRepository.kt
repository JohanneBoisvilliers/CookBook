package com.example.cookbook.repositories

import com.example.cookbook.database.dao.PhotoDao
import com.example.cookbook.models.Photo

class PhotoDataRepository(val photoDao: PhotoDao){
    suspend fun insertPhoto(vararg photo: Photo){
        photoDao.insertPhoto(*photo)
    }
}