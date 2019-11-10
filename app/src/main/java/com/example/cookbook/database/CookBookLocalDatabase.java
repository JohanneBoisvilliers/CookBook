package com.example.cookbook.database;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.cookbook.R;
import com.example.cookbook.database.dao.IngredientDao;
import com.example.cookbook.database.dao.RecipeDao;
import com.example.cookbook.database.dao.UserDao;
import com.example.cookbook.models.Ingredient;
import com.example.cookbook.models.Recipe;
import com.example.cookbook.utils.MyApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;


@Database(entities = {Ingredient.class, Recipe.class}, version = 1, exportSchema = false)
public abstract class CookBookLocalDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile CookBookLocalDatabase INSTANCE;

    // --- DAO ---
    public abstract UserDao userDao();
    public abstract IngredientDao ingredientDao();

    public abstract RecipeDao recipeDao();

    // --- INSTANCE ---
    public static CookBookLocalDatabase getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (CookBookLocalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CookBookLocalDatabase.class, "MyDatabase.db")
                            .addCallback(new Callback(){
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    insertIngredientHelper(db);
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("id", 1);
                                    contentValues.put("name", "Oeuf au plat");
                                    db.insert("Recipe", OnConflictStrategy.IGNORE, contentValues);
                            }})
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void insertIngredientHelper(SupportSQLiteDatabase db){

        InputStream is = MyApp.getContext().getResources().openRawResource(R.raw.ingredients);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";
        int lineNumber = 0;
        try {
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] tokens = line.split(",");
                lineNumber += 1;

                ContentValues ingredientValue=new ContentValues();
                ingredientValue.put("id",lineNumber);
                ingredientValue.put("name",tokens[0]);
                db.insert("Ingredient",OnConflictStrategy.REPLACE,ingredientValue);

            }
        } catch (IOException e) {
            Log.wtf("deb", "Error reading data file on line" + line, e);
            e.printStackTrace();
        }
    }
}
