package com.example.cookbook.addRecipePage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.cookbook.models.BaseDataRecipe
import com.example.cookbook.models.Ingredient
import com.example.cookbook.models.IngredientDatabase
import com.example.cookbook.repositories.IngredientDataRepository
import com.example.cookbook.repositories.RecipesDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AddRecipeViewModel(private val mIngredientDataRepository: IngredientDataRepository,private val mRecipesDataRepository: RecipesDataRepository):ViewModel(){

    val recipeName = MutableLiveData<String>()
    val recipeUrl = MutableLiveData("")
    val category = MutableLiveData<String>()
    val quantity = MutableLiveData<Int>()
    val unit = MutableLiveData<String>()

    fun insertRecipe(recipe: BaseDataRecipe):Long{
        return runBlocking { mRecipesDataRepository.insertRecipe(recipe) }
    }
}