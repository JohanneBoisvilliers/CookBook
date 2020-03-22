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
import com.example.cookbook.models.BaseDataRecipe;
import com.example.cookbook.models.BaseRecipeIngredientCrossRef;
import com.example.cookbook.models.Ingredient;
import com.example.cookbook.models.IngredientDataInfoCrossRef;
import com.example.cookbook.models.IngredientDatabase;
import com.example.cookbook.models.IngredientDetails;
import com.example.cookbook.models.Photo;
import com.example.cookbook.models.Step;
import com.example.cookbook.utils.MyApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;


@Database(entities = {
        Ingredient.class,
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
                                    insertRecipeHelper(db);
                                    //insertPhotoHelper(db);
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
                recette.put("isOnline", tokens[4]);
                db.insert("BaseDataRecipe", OnConflictStrategy.REPLACE, recette);

            }
        } catch (IOException e) {
            Log.wtf("deb", "Error reading data file on line" + line, e);
            e.printStackTrace();
        }
    }

    private static void insertPhotoHelper(SupportSQLiteDatabase db){

        ContentValues photo1 = new ContentValues();
        photo1.put("id",1);
        photo1.put("recipeId",1);
        photo1.put("photoUrl","storage/emulated/0/DCIM/photo_1.jpg");
        db.insert("Photo", OnConflictStrategy.REPLACE, photo1);
        ContentValues photo2 = new ContentValues();
        photo1.put("id",2);
        photo1.put("recipeId",2);
        photo1.put("photoUrl","storage/emulated/0/DCIM/photo_2.jpg");
        db.insert("Photo", OnConflictStrategy.REPLACE, photo2);
        ContentValues photo3 = new ContentValues();
        photo1.put("id",3);
        photo1.put("recipeId",3);
        photo1.put("photoUrl","storage/emulated/0/DCIM/photo_3.jpg");
        db.insert("Photo", OnConflictStrategy.REPLACE, photo3);
        ContentValues photo4 = new ContentValues();
        photo1.put("id",4);
        photo1.put("recipeId",4);
        photo1.put("photoUrl","storage/emulated/0/DCIM/photo_4.jpg");
        db.insert("Photo", OnConflictStrategy.REPLACE, photo4);
        ContentValues photo5 = new ContentValues();
        photo1.put("id",5);
        photo1.put("recipeId",5);
        photo1.put("photoUrl","storage/emulated/0/DCIM/photo_5.jpg");
        db.insert("Photo", OnConflictStrategy.REPLACE, photo5);
        ContentValues photo6 = new ContentValues();
        photo1.put("id",6);
        photo1.put("recipeId",6);
        photo1.put("photoUrl","storage/emulated/0/DCIM/photo_6.jpg");
        db.insert("Photo", OnConflictStrategy.REPLACE, photo6);

    }

}
