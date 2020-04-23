package com.example.cookbook.settingsPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.repositories.FirestoreUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel (private val mFirestoreUserRepository: FirestoreUserRepository): ViewModel() {
    fun updateUserProfile(username:String, photoUrl:String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                mFirestoreUserRepository.updateUser(username,photoUrl)
            }
        }
    }
}