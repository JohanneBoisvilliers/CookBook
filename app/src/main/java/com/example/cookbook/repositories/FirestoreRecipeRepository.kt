package com.example.cookbook.repositories

import android.net.Uri
import com.example.cookbook.models.HeadLineArticle
import com.example.cookbook.models.Ingredient
import com.example.cookbook.models.Recipe
import com.example.cookbook.models.Step
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FirestoreRecipeRepository {
    var firestoreDB = FirebaseFirestore.getInstance()
    var firebaseUser = FirebaseAuth.getInstance().currentUser
    var storage = Firebase.storage
    var storageRef = storage.reference

    suspend fun getSharedRecipe():MutableList<Map<String,Any>>{
        val list = firestoreDB.collection("sharedRecipes")
                .get()
                .addOnSuccessListener {
                    println("success")
                }
                .addOnFailureListener{ println(it)}.await()

        return list.documents.map { x -> x.data!! }.toMutableList()
    }

    fun sharedRecipe(recipe: Recipe, description: String, photoUrls: List<String>,ingredientList: List<Ingredient>): Task<Void> {
        val sharedRecipeInformation = hashMapOf(
                "user_Id" to firebaseUser?.uid,
                "username" to firebaseUser?.displayName,
                "user_profile_photo" to firebaseUser?.photoUrl.toString(),
                "recipe" to recipe,
                "shared_date" to getCurrentDateTime().dateToString("dd/MM/yyyy"),
                "description" to description,
                "photosUrl" to photoUrls,
                "ingredient_list" to convertIngredientIntoString(ingredientList.toMutableList()),
                "step_list" to convertStepIntoString(recipe.stepList)
        )
        var documentReference =
                firestoreDB
                        .collection("sharedRecipes")
                        .document()
        return documentReference.set(sharedRecipeInformation)
    }

    suspend fun uploadPhoto(recipe: Recipe, file: File): Uri {
        val newFile = Uri.fromFile(file)
        val photoRef = storageRef.child(
                "${firebaseUser?.uid}/${recipe.baseDataRecipe?.name}/${newFile.lastPathSegment}")
        val uploadTask = photoRef.putFile(newFile)

        return uploadTask
                .addOnSuccessListener { println("Success") }
                .addOnFailureListener { println("Failed") }
                .continueWithTask {
                    photoRef.downloadUrl
                }.addOnCompleteListener {
                    it.result!!
                }.await()
    }

    suspend fun getHeadLineArticle():HeadLineArticle{
        val headline = firestoreDB.collection("head_line_article")
                .orderBy("sharedDate", Query.Direction.DESCENDING)
                .limit(1)
                .get().await()

         return headline.documents[0]!!
                 .toObject<HeadLineArticle>()!!
    }

    //-------------------- UTILS ------------------------

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    private fun convertIngredientIntoString(ingredientList:MutableList<Ingredient>) : List<String>{
        val convertedIngredientList = mutableListOf<String>()
        for (ingredient in ingredientList) {
            convertedIngredientList.add(
                    "${ingredient.ingredientData.quantity} " +
                            ingredient.ingredientData.unit + " " +
                            ingredient.ingredientDatabase.name
            )
        }
        return convertedIngredientList
    }

    private fun convertStepIntoString(stepList:MutableList<Step>) : List<String>{
        val convertedSteplist = mutableListOf<String>()
        for (step in stepList) {
            convertedSteplist.add(
                    step.description
            )
        }
        return convertedSteplist
    }

    //-------------------- EXTENSIONS ------------------------

    fun Date.dateToString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
}