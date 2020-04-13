package com.example.cookbook.recipeDetailsPage

import android.net.Uri
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.cookbook.R
import com.example.cookbook.models.Photo
import com.example.cookbook.models.Recipe
import com.example.cookbook.utils.GlideApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.recyclerview_all_recipes_item.view.*
import kotlinx.android.synthetic.main.viewpager_recipe_photo_item.view.*
import kotlinx.android.synthetic.main.viewpager_recipe_photo_item.view.photo_container
import java.io.File

class PhotoViewPagerViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(photo: T) {
        this.photoSettings(photo)
    }

    //get the actual recipe and check if there are some photos to
    //set the recipe thumbnail
    fun photoSettings(photo: T) {
        val httpsReference = Firebase.storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/cookbook-92037.appspot.com/o/vpAwThITFWNpGke8JIPSJ1kNe9C2%2FTartine%20d'avocat%2Fphoto_4.jpg?alt=media&token=f19095a8-e6a1-4e5b-9fc0-e0ffdfdb7b47")

        if (photo is Photo) {
            initGlide(Uri.fromFile(File(photo.photoUrl)))
        }else{
            initGlide(photo as String)
        }
    }

    private fun <T>initGlide(url:T){
        val circularProgressDrawable = CircularProgressDrawable(itemView.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide.with(itemView.context)
                .load(url)
                .placeholder(circularProgressDrawable)
                .error(R.color.colorAccent)
                .centerCrop()
                .into(itemView.photo_container)
    }
}