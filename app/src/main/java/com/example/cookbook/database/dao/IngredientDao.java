package com.example.cookbook.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cookbook.models.Ingredient;

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM Ingredient WHERE ingredientId = :ingredientId")
    LiveData<Ingredient> getIngredient(long ingredientId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertIngredient(Ingredient ingredient);
}
