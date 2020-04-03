package com.example.cookbook.injections;

import android.content.Context;

import com.example.cookbook.database.CookBookLocalDatabase;
import com.example.cookbook.repositories.IngredientDataRepository;
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
    //instantiate ingredient repository
    public static StepDataRepository provideStepDataRepository(Context context) {
        CookBookLocalDatabase database = CookBookLocalDatabase.getInstance(context);
        return new StepDataRepository(database.stepDao());
    }
    //instantiate viewmodel factory
    public static ViewModelFactory provideViewModelFactory(Context context) {
        RecipesDataRepository recipesDataRepository = provideRecipesDataSource(context);
        IngredientDataRepository ingredientDataRepository = provideIngredientDataSource(context);
        StepDataRepository stepDataRepository = provideStepDataRepository(context);
        return new ViewModelFactory(recipesDataRepository, ingredientDataRepository,stepDataRepository);
    }
}
