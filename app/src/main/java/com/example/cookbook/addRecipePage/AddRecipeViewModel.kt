package com.example.cookbook.addRecipePage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookbook.models.Ingredient

class AddRecipeViewModel:ViewModel(){

    val recipeName = MutableLiveData<String>()
    val category = MutableLiveData<String>()
    val boolean = MutableLiveData<Boolean>()
    val quantity = MutableLiveData<Int>()
    val unit = MutableLiveData<String>()
    val ingredientName = MutableLiveData<String>()
    val ingredientList = MutableLiveData<MutableList<Ingredient>>()
}