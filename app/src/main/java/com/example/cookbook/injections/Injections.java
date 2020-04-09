package com.example.cookbook.injections;

import android.content.Context;

import com.example.cookbook.database.CookBookLocalDatabase;
import com.example.cookbook.repositories.FirestoreRecipeRepository;
import com.example.cookbook.repositories.IngredientDataRepository;
import com.example.cookbook.repositories.PhotoDataRepository;
import com.example.cookbook.repositories.RecipesDataRepository;
import com.example.cookbook.repositories.StepDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injections {
    //instantiate recipe repository
    public static RecipesDataRepository provideRecipesDataSource(Context context) {
        CookBookLocalDatabase database = CookBookLocalDatabase.getInstance(context);
        return new RecipesDataRepository(database.recipeDao());
    }
    //instantiate ingredient repository
    public static IngredientDataRepository provideIngredientDataSource(Context context) {
        CookBookLocalDatabase database = CookBookLocalDatabase.getInstance(context);
        return new IngredientDataRepository(database.ingredientDao());
    }
    //instantiate step repository
    public static StepDataRepository provideStepDataRepository(Context context) {
        CookBookLocalDatabase database = CookBookLocalDatabase.getInstance(context);
        return new StepDataRepository(database.stepDao());
    }
    //instantiate photo repository
    public static PhotoDataRepository providePhotoDataRepository(Context context) {
        CookBookLocalDatabase database = CookBookLocalDatabase.getInstance(context);
        return new PhotoDataRepository(database.photoDao());
    }
    //instantiate photo repository
    public static FirestoreRecipeRepository provideFireStoreRecipeDataRepository(Context context) {
        CookBookLocalDatabase database = CookBookLocalDatabase.getInstance(context);
        return new FirestoreRecipeRepository();
    }
    //instantiate viewmodel factory
    public static ViewModelFactory provideViewModelFactory(Context context) {
        RecipesDataRepository recipesDataRepository = provideRecipesDataSource(context);
        IngredientDataRepository ingredientDataRepository = provideIngredientDataSource(context);
        StepDataRepository stepDataRepository = provideStepDataRepository(context);
        PhotoDataRepository photoDataRepository = providePhotoDataRepository(context);
        FirestoreRecipeRepository firestoreRecipeRepository = provideFireStoreRecipeDataRepository(context);
        return new ViewModelFactory(
                recipesDataRepository,
                ingredientDataRepository,
                stepDataRepository,
                photoDataRepository,
                firestoreRecipeRepository
        );
    }
}
