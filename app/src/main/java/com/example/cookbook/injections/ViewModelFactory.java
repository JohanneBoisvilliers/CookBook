package com.example.cookbook.injections;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cookbook.loginPage.LoginViewModel;
import com.example.cookbook.profilePage.ProfileViewModel;
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
        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            return (T) new ProfileViewModel();
        }
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
