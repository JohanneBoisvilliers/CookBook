package com.example.cookbook.injections;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cookbook.loginPage.LoginViewModel;
import com.example.cookbook.profilePage.ProfileViewModel;
import com.example.cookbook.addRecipePage.AddRecipeViewModel;
import com.example.cookbook.recipesPage.RecipeViewModel;
import com.example.cookbook.repositories.FirestoreRecipeRepository;
import com.example.cookbook.repositories.FirestoreUserRepository;
import com.example.cookbook.repositories.IngredientDataRepository;
import com.example.cookbook.repositories.PhotoDataRepository;
import com.example.cookbook.repositories.RecipesDataRepository;
import com.example.cookbook.repositories.StepDataRepository;
import com.example.cookbook.settingsPage.SettingsViewModel;
import com.example.cookbook.socialPage.SocialViewModel;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private RecipesDataRepository mRecipesDataRepository;
    private IngredientDataRepository mIngredientDataRepository;
    private StepDataRepository mStepDataRepository;
    private PhotoDataRepository mPhotoDataRepository;
    private FirestoreRecipeRepository mFireStoreRecipeRepository;
    private FirestoreUserRepository mFireStoreUserRepository;

    public ViewModelFactory(RecipesDataRepository recipesDataRepository,
                            IngredientDataRepository ingredientDataRepository,
                            StepDataRepository stepDataRepository,
                            PhotoDataRepository photoDataRepository,
                            FirestoreRecipeRepository firestoreRecipeRepository,
                            FirestoreUserRepository firestoreUserRepository) {
        this.mRecipesDataRepository = recipesDataRepository;
        this.mIngredientDataRepository = ingredientDataRepository;
        this.mStepDataRepository = stepDataRepository;
        this.mPhotoDataRepository = photoDataRepository;
        this.mFireStoreRecipeRepository = firestoreRecipeRepository;
        this.mFireStoreUserRepository = firestoreUserRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipeViewModel.class)) {
            return (T) new RecipeViewModel(mRecipesDataRepository, mIngredientDataRepository, mStepDataRepository,mPhotoDataRepository,mFireStoreRecipeRepository);
        }
        if (modelClass.isAssignableFrom(AddRecipeViewModel.class)) {
            return (T) new AddRecipeViewModel(mIngredientDataRepository, mRecipesDataRepository);
        }
        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            return (T) new ProfileViewModel();
        }
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel();
        }
        if (modelClass.isAssignableFrom(SocialViewModel.class)) {
            return (T) new SocialViewModel(mFireStoreRecipeRepository);
        }
        if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(mFireStoreUserRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
