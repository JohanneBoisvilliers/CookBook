package com.example.cookbook.injections;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cookbook.addRecipePage.AddRecipeViewModel;
import com.example.cookbook.recipesPage.RecipeViewModel;
import com.example.cookbook.repositories.IngredientDataRepository;
import com.example.cookbook.repositories.RecipesDataRepository;
import com.example.cookbook.repositories.StepDataRepository;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private RecipesDataRepository mRecipesDataRepository;
    private IngredientDataRepository mIngredientDataRepository;
    private StepDataRepository mStepDataRepository;

    public ViewModelFactory(RecipesDataRepository recipesDataRepository,
                            IngredientDataRepository ingredientDataRepository,
                            StepDataRepository stepDataRepository) {
        this.mRecipesDataRepository = recipesDataRepository;
        this.mIngredientDataRepository = ingredientDataRepository;
        this.mStepDataRepository = stepDataRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipeViewModel.class)) {
            return (T) new RecipeViewModel(mRecipesDataRepository, mIngredientDataRepository, mStepDataRepository);
        }
        if (modelClass.isAssignableFrom(AddRecipeViewModel.class)) {
            return (T) new AddRecipeViewModel(mIngredientDataRepository, mRecipesDataRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
