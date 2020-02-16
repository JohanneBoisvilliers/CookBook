package com.example.cookbook.recipesPage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.cookbook.models.Recipe;
import com.example.cookbook.repositories.RecipesDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class RecipeViewModel extends ViewModel {
    private Executor mExecutor;
    private RecipesDataRepository mRecipesDataRepository;

    public RecipeViewModel(Executor executor, RecipesDataRepository recipesDataRepository) {
        this.mExecutor = executor;
        this.mRecipesDataRepository = recipesDataRepository;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipesDataRepository.getRecipes();
    }
}
