package com.example.cookbook.socialPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.repositories.FirestoreRecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SocialViewModel(val mFirestoreRecipeRepository: FirestoreRecipeRepository):ViewModel(){

    lateinit var sharedRecipesList : List<Map<String,Any>>

    fun getSharedRecipes(){
        viewModelScope.launch (Dispatchers.IO){
            sharedRecipesList = mFirestoreRecipeRepository.getSharedRecipe()
        }
    }
}