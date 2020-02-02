package com.example.cookbook.recipesPage

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.cookbook.R
import kotlinx.android.synthetic.main.recyclerview_all_recipes_item.view.*
import kotlinx.android.synthetic.main.activity_main.*


class RecipesListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val mPhotoContainer = itemView.photo_container
}