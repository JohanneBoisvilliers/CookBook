package com.example.cookbook.repositories

import com.example.cookbook.models.Recipe
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRecipeRepository {
    var firestoreDB = FirebaseFirestore.getInstance()


    fun sharedRecipe(recipe: Recipe,description: String): Task<Void> {
        val test = hashMapOf(
                "recipe" to recipe,
                "description" to description
        )
        var documentReference =
                firestoreDB
                        .collection("sharedRecipes")
                        .document(recipe.baseDataRecipe?.name!!)
        return documentReference.set(test)
    }
}