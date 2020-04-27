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
import com.example.cookbook.database.dao.PhotoDao;
import com.example.cookbook.database.dao.RecipeDao;
import com.example.cookbook.database.dao.StepDao;
import com.example.cookbook.database.dao.UserDao;
import com.example.cookbook.models.BaseDataRecipe;
import com.example.cookbook.models.BaseRecipeIngredientCrossRef;
import com.example.cookbook.models.IngredientDatabase;
import com.example.cookbook.models.IngredientData;
import com.example.cookbook.models.Photo;
import com.example.cookbook.models.Step;
import com.example.cookbook.utils.MyApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;


@Database(entities = {
        IngredientData.class,
        IngredientDatabase.class,
        BaseDataRecipe.class,
        Photo.class,
        BaseRecipeIngredientCrossRef.class,
        Step.class},
        version = 1,
        exportSchema = false)
public abstract class CookBookLocalDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile CookBookLocalDatabase INSTANCE;

    // --- DAO ---
    public abstract IngredientDao ingredientDao();
    public abstract RecipeDao recipeDao();
    public abstract StepDao stepDao();
    public abstract PhotoDao photoDao();

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
                                    insertRecipeHelper(db);
                                    //insertPhotoHelper(db);
                            }})
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void insertIngredientHelper(SupportSQLiteDatabase db){

        InputStream is = MyApp.getContext().getResources().openRawResource(R.raw.ingredients);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";
        int lineNumber = 0;
        try {
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] tokens = line.split(";");
                lineNumber += 1;

                ContentValues ingredientValue=new ContentValues();
                ingredientValue.put("ingredientDatabaseId",lineNumber);
                ingredientValue.put("name",tokens[0]);
                db.insert("IngredientDatabase",OnConflictStrategy.REPLACE,ingredientValue);

            }
        } catch (IOException e) {
            Log.wtf("deb", "Error reading data file on line" + line, e);
            e.printStackTrace();
        }
    }

    private static void insertRecipeHelper(SupportSQLiteDatabase db) {

        InputStream is = MyApp.getContext().getResources().openRawResource(R.raw.recettes);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";
        int lineNumber = 0;
        try {
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] tokens = line.split(";");
                lineNumber += 1;

                ContentValues recette = new ContentValues();
                recette.put("baseRecipeId", lineNumber);
                recette.put("name", tokens[0]);
                recette.put("isAlreadyDone", tokens[1]);
                recette.put("numberOfLike", tokens[2]);
                recette.put("addDate", tokens[3]);
                recette.put("recipeUrl", tokens[4]);
                recette.put("category", tokens[5]);
                db.insert("BaseDataRecipe", OnConflictStrategy.REPLACE, recette);

            }
        } catch (IOException e) {
            Log.wtf("deb", "Error reading data file on line" + line, e);
            e.printStackTrace();
        }
    }

}
