package com.example.cookbook.profilePage

import androidx.lifecycle.*
import com.example.cookbook.models.Recipe
import com.example.cookbook.repositories.FirestoreRecipeRepository
import com.example.cookbook.repositories.RecipesDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class ProfileViewModel(private val mRecipeRepository: RecipesDataRepository) : ViewModel(){
    val name = MutableLiveData<String>()
    val creationDate = MutableLiveData<Long>()
    val starterNumber = MutableLiveData(0)
    val mainNumber = MutableLiveData(0)
    val dessertNumber = MutableLiveData(0)
    val drinkNumber = MutableLiveData(0)
    val recipes: LiveData<List<Recipe>> = mRecipeRepository.recipes

}