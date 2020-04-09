package com.example.cookbook.repositories

import com.example.cookbook.models.Recipe
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRecipeRepository {
    var firestoreDB = FirebaseFirestore.getInstance()


    fun sharedRecipe(recipe: Recipe): Task<Void> {
        var documentReference =
                firestoreDB
                        .collection("sharedRecipes")
                        .document(recipe.baseDataRecipe?.name!!)
        return documentReference.set(recipe)
    }
}