package com.example.cookbook.recipeDetailsPage

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookbook.R
import com.example.cookbook.models.Photo
import com.example.cookbook.models.Recipe
import kotlinx.android.synthetic.main.recyclerview_all_recipes_item.view.*
import kotlinx.android.synthetic.main.viewpager_recipe_photo_item.view.*
import kotlinx.android.synthetic.main.viewpager_recipe_photo_item.view.photo_container
import java.io.File

class PhotoViewPagerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    constructor(parent: ViewGroup) :
            this(LayoutInflater.from(parent.context).inflate(R.layout.viewpager_recipe_photo_item, parent, false))

    fun bind(photo: Photo){
        this.PhotoSettings(photo)
    }

    //get the actual recipe and check if there are some photos to
    //set the recipe thumbnail
    fun PhotoSettings(photo:Photo){
        if(photo != null){
            Glide.with(itemView.context)
                    .load(Uri.fromFile(File(photo.photoUrl)))
                    .error(R.drawable.assiette_vide)
                    .centerCrop()
                    .into(itemView.photo_container)
        }else{
            itemView.photo_container.setImageResource(R.drawable.assiette_vide)
        }
    }
}