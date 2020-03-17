package com.example.cookbook.injections;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cookbook.addRecipePage.AddRecipeViewModel;
import com.example.cookbook.recipesPage.RecipeViewModel;
import com.example.cookbook.repositories.IngredientDataRepository;
import com.example.cookbook.repositories.RecipesDataRepository;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private RecipesDataRepository mRecipesDataRepository;
    private IngredientDataRepository mIngredientDataRepository;

    public ViewModelFactory(RecipesDataRepository recipesDataRepository, IngredientDataRepository ingredientDataRepository) {
        this.mRecipesDataRepository = recipesDataRepository;
        this.mIngredientDataRepository = ingredientDataRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipeViewModel.class)) {
            return (T) new RecipeViewModel(mRecipesDataRepository,mIngredientDataRepository);
        }
        if (modelClass.isAssignableFrom(AddRecipeViewModel.class)) {
            return (T) new AddRecipeViewModel(mIngredientDataRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
