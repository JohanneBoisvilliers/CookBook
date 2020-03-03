package com.example.cookbook.loginPage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel(){
    lateinit var isComeFromLogin:MutableLiveData<Boolean>
}