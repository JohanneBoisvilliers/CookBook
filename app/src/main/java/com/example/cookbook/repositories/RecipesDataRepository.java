package com.example.cookbook.repositories;

import androidx.lifecycle.LiveData;

import com.example.cookbook.database.dao.RecipeDao;
import com.example.cookbook.models.Recipe;

public class RecipesDataRepository {
    private final RecipeDao mRecipeDao;

    public RecipesDataRepository(RecipeDao recipeDao) {
        this.mRecipeDao = recipeDao;
    }

    public LiveData<Recipe> getRecipes() {
        return this.mRecipeDao.getRecipes();
    }
}
