package com.example.cookbook.repositories;

import androidx.lifecycle.LiveData;

import com.example.cookbook.database.dao.RecipeDao;
import com.example.cookbook.models.Recipe;

import java.util.List;

public class RecipesDataRepository {
    private final RecipeDao mRecipeDao;

    public RecipesDataRepository(RecipeDao recipeDao) {
        this.mRecipeDao = recipeDao;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return this.mRecipeDao.getRecipes();
    }
}
