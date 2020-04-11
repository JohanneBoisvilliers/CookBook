package com.example.cookbook.repositories

import android.net.Uri
import com.example.cookbook.models.Recipe
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File

class FirestoreRecipeRepository {
    var firestoreDB = FirebaseFirestore.getInstance()
    var firebaseUser = FirebaseAuth.getInstance().currentUser
    var storage = Firebase.storage
    var storageRef = storage.reference

    fun sharedRecipe(recipe: Recipe,description: String,photoUrls:List<String>): Task<Void> {
        val sharedRecipeInformation = hashMapOf(
                "user" to firebaseUser?.uid,
                "recipe" to recipe,
                "description" to description,
                "photosUrl" to photoUrls
        )
        var documentReference =
                firestoreDB
                        .collection("sharedRecipes")
                        .document()
        return documentReference.set(sharedRecipeInformation)
    }

    suspend fun uploadPhoto(recipe:Recipe, file: File):Uri{
        val newFile = Uri.fromFile(file)
        val photoRef = storageRef.child(
                "${firebaseUser?.uid}/${recipe.baseDataRecipe?.name}/${newFile.lastPathSegment}")
        val uploadTask = photoRef.putFile(newFile)

        return uploadTask
                .addOnSuccessListener { println("Success") }
                .addOnFailureListener { println("Failed") }
                .continueWithTask {
            photoRef.downloadUrl
        }.addOnCompleteListener{
             it.result!!
        }.await()
    }
}