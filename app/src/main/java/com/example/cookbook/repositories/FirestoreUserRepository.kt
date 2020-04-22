package com.example.cookbook.repositories

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreUserRepository {
    var firestoreDB = FirebaseFirestore.getInstance()
    var currentUser = FirebaseAuth.getInstance().currentUser

     fun updateUser(username:String, photoUrl:String){
        val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(Uri.parse(photoUrl))
                .build()

        currentUser?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        println("User profile updated.")
                    }
                }
    }
}