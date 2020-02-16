package com.example.cookbook.injections;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cookbook.recipesPage.RecipeViewModel;
import com.example.cookbook.repositories.RecipesDataRepository;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private RecipesDataRepository mRecipesDataRepository;
    private Executor mExecutor;

    public ViewModelFactory(RecipesDataRepository recipesDataRepository, Executor executor) {
        this.mRecipesDataRepository = recipesDataRepository;
        this.mExecutor = executor;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipeViewModel.class)) {
            return (T) new RecipeViewModel(mExecutor, mRecipesDataRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
