package com.example.cookbook.repositories

import android.net.Uri
import com.example.cookbook.models.*
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

class FirestoreRecipeRepository {
    var firestoreDB = FirebaseFirestore.getInstance()
    var currentUser = FirebaseAuth.getInstance().currentUser
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

    suspend fun sharedRecipe(recipe: Recipe, description: String, photoUrls: List<String>,ingredientList: List<Ingredient>): Task<Void> {
        val sharedRecipeInformation = hashMapOf(
                "user_id" to currentUser?.uid,
                "username" to currentUser?.displayName,
                "user_profile_photo" to currentUser?.photoUrl.toString(),
                "recipe" to recipe,
                "shared_date" to getCurrentDateTime().dateToString("dd/MM/yyyy"),
                "description" to description,
                "photosUrl" to photoUrls,
                "ingredient_list" to convertIngredientIntoString(ingredientList.toMutableList()),
                "step_list" to convertStepIntoString(recipe.stepList),
                "users_liked" to listOf<String>()
        )
        var documentReference =
                firestoreDB
                        .collection("sharedRecipes")
                        .document()
        documentReference.set(sharedRecipeInformation).continueWithTask{
            val data = hashMapOf("document_id" to documentReference.id)
            documentReference.set(data, SetOptions.merge())
        }.await()
        var counterRef= documentReference.collection("counter").document("${documentReference.id}_counter")
        return createCounter(counterRef,5)
    }

    suspend fun uploadPhoto(recipe: Recipe, file: File): Uri {
        val newFile = Uri.fromFile(file)
        val photoRef = storageRef.child(
                "${currentUser?.uid}/${recipe.baseDataRecipe?.name}/${newFile.lastPathSegment}")
        val uploadTask = photoRef.putFile(newFile)

        return uploadTask
                .addOnSuccessListener { println("Photo upload success") }
                .addOnFailureListener { println("Photo upload failed") }
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

    private fun createCounter(ref: DocumentReference, numShards: Int): Task<Void> {
        // Initialize the counter document, then initialize each shard.
        return ref.set(Counter(numShards))
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }

                    val tasks = arrayListOf<Task<Void>>()

                    // Initialize each shard with count=0
                    for (i in 0 until numShards) {
                        val makeShard = ref.collection("shards")
                                .document(i.toString())
                                .set(Shard(0))

                        tasks.add(makeShard)
                    }

                    Tasks.whenAll(tasks)
                }
    }

     fun likeRecipe(counterDocRef: DocumentReference,
                    recipeDocRef: DocumentReference,
                    numShards: Int,
                    isChecked:Boolean): Task<Void> {
        val shardId = floor(Math.random() * numShards).toInt()
        val shardRef = counterDocRef.collection("shards").document(shardId.toString())
         val action =
                 if (isChecked)
                     FieldValue.arrayUnion(currentUser?.uid)
                 else
                     FieldValue.arrayRemove(currentUser?.uid)

         return firestoreDB.runBatch {
             recipeDocRef.update("users_liked", action)
             shardRef.update("count", FieldValue.increment(if(isChecked) 1L else -1L))
         }
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