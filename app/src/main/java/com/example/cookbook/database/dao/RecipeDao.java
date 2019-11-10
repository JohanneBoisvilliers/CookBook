package com.example.cookbook.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.cookbook.models.Recipe;

@Dao
public interface RecipeDao {
    @Query("SELECT * FROM Recipe")
    LiveData<Recipe> getRecipes();
}
