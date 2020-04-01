package com.example.cookbook.models

import androidx.room.*

 class Ingredient(
        @Embedded
        var ingredientData: IngredientData,
        @Relation(parentColumn = "ingredientDatabaseId",entityColumn = "ingredientDatabaseId")
        var ingredientDatabase: IngredientDatabase
//        @PrimaryKey(autoGenerate = true)
//        var ingredientId:Long=0L,
//        var recipeId:Long,
//        var ingredientDatabaseId:Long,
//        var unit: String,
//        var quantity: Int,
//        @Ignore
//        var ingredientDatabase: IngredientDatabase?
)
//{
//     constructor():this(0,0,0,"gr",0,IngredientDatabase())
//}
