package com.example.cookbook.recipeDetailsPage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.models.Ingredient
import kotlinx.android.synthetic.main.recyclerview_ingredient_item.view.*

class IngredientsListViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

 fun bind(ingredient: Ingredient){
     itemView.ingredient_quantity.text = ingredient.quantity.toString()
     itemView.ingredient_unit.text = ingredient.unit
 }
}