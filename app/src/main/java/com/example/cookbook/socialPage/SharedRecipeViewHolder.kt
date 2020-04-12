package com.example.cookbook.socialPage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookbook.R
import kotlinx.android.synthetic.main.recyclerview_social_item.view.*


class SharedRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    //set the recipe's item in recycler view
    fun updateRecipeCardUi(recipe: Map<String, Any>) {
        val recipeMap = recipe["recipe"] as Map<String, Any>
        val baseDataRecipeMap = recipeMap["baseDataRecipe"] as Map<String, Any>
        val photosList = recipe["photosUrl"] as List<String>
        itemView.category_container.text = baseDataRecipeMap["category"].toString()
        itemView.social_description.text = recipe["description"].toString()
        itemView.posted_by.text = itemView.context.getString(R.string.posted_by, recipe["username"].toString())
        this.recipePhotoSettings(photosList[0])
        this.userPhotoSettings(recipe["user_profile_photo"].toString())
        itemView.shared_date.text = recipe["shared_date"].toString()
    }

    //get the actual recipe and check if there are some photos to
    //set the recipe thumbnail
    fun recipePhotoSettings(url: String) {
        Glide.with(itemView.context)
                .load(url)
                .error(R.drawable.no_photo_low)
                .centerCrop()
                .into(itemView.recipe_photo)
    }

    fun userPhotoSettings(url: String) {
        Glide.with(itemView.context)
                .load(url)
                .error(R.drawable.no_photo_low)
                .circleCrop()
                .into(itemView.user_photo)
    }
}