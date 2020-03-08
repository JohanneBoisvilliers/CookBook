package com.example.cookbook.loginPage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    val isLoginExpanded = MutableLiveData<Boolean>(false)
    val isRegisterExpanded = MutableLiveData<Boolean>(false)

}