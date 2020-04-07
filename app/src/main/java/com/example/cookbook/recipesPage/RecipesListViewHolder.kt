package com.example.cookbook.recipesPage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cookbook.R
import com.example.cookbook.models.Recipe
import kotlinx.android.synthetic.main.recyclerview_all_recipes_item.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException


class RecipesListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    //set the recipe's item in recycler view
    fun updateRecipeCardUi(recipe:Recipe){
        itemView.recipe_title.text= recipe.baseDataRecipe?.name
        this.PhotoSettings(recipe)
    }
    //get the actual recipe and check if there are some photos to
    //set the recipe thumbnail
    fun PhotoSettings(recipe:Recipe){
        if(recipe.photoList.size > 0){
            Glide.with(itemView.context)
                    .load(Uri.fromFile(File(recipe.photoList[0].photoUrl)))
                    .error(R.drawable.no_photo_low)
                    .centerCrop()
                    .into(itemView.photo_container)
        }else{
            Glide.with(itemView.context)
                    .load(R.drawable.no_photo_low)
                    .fitCenter()
                    .into(itemView.photo_container)
        }
    }
}