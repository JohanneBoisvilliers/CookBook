package com.example.cookbook.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.cookbook.models.Step

@Dao
interface StepDao {
    @Insert
    suspend fun addStep(step:Step):Long
}