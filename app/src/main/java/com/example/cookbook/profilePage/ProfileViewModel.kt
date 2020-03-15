package com.example.cookbook.profilePage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class ProfileViewModel : ViewModel(){
    val name = MutableLiveData<String>()
    val creationDate = MutableLiveData<Long>()

    fun getDate():String{
        val nonFormatDate = Date(creationDate.value!!)
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return  formatter.format(nonFormatDate)
    }
}