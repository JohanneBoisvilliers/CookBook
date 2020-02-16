package com.example.cookbook.injections;

import android.content.Context;

import com.example.cookbook.database.CookBookLocalDatabase;
import com.example.cookbook.repositories.RecipesDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injections {
    public static RecipesDataRepository provideRecipesDataSource(Context context) {
        CookBookLocalDatabase database = CookBookLocalDatabase.getInstance(context);
        return new RecipesDataRepository(database.recipeDao());
    }

    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        RecipesDataRepository recipesDataRepository = provideRecipesDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(recipesDataRepository, executor);
    }
}
