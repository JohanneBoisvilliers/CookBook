package com.example.cookbook.homePage

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookbook.R
import com.example.cookbook.models.Recipe
import kotlinx.android.synthetic.main.recyclerview_horizontal_home_item.view.photo_container
import kotlinx.android.synthetic.main.recyclerview_horizontal_home_item.view.recipe_category
import kotlinx.android.synthetic.main.recyclerview_horizontal_home_item.view.recipe_title
import java.io.File

class HorizontalRecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun updateUiOfRecipe(recipe: Recipe) {
        itemView.recipe_category.text = recipe.baseDataRecipe?.category
        itemView.recipe_title.text = recipe.baseDataRecipe?.name
        photoSettings(recipe)
    }

    //get the actual recipe and check if there are some photos to
    //set the recipe thumbnail
    private fun photoSettings(recipe:Recipe){
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