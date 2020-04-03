package com.example.cookbook.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.cookbook.models.Step

@Dao
interface StepDao {
    @Insert
    suspend fun addStep(step:Step):Long

    @Update
    suspend fun updateStep(step: Step)

    @Delete
    suspend fun deleteStep(step: Step)
}