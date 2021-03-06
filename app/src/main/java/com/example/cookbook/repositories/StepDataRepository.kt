package com.example.cookbook.repositories

import com.example.cookbook.database.dao.StepDao
import com.example.cookbook.models.Step

class StepDataRepository(private val stepDao: StepDao) {

    suspend fun addStep(step: Step): Long {
        return stepDao.addStep(step)
    }

    suspend fun updateStep(step: Step){
        stepDao.updateStep(step)
    }

    suspend fun deleteStep(step: Step){
        stepDao.deleteStep(step)
    }

}