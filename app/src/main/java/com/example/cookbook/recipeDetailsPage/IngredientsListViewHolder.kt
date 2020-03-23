package com.example.cookbook.recipeDetailsPage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.models.Ingredient
import kotlinx.android.synthetic.main.recyclerview_ingredient_item.view.*

class IngredientsListViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

 fun bind(ingredient: Ingredient,isEditModeOn:Boolean){
     itemView.ingredient_quantity.text = ingredient.quantity.toString()
     itemView.ingredient_unit.text = ingredient.unit
     itemView.ingredient_name.text = ingredient.ingredientDatabase?.name

     itemView.update_icon.visibility = if(isEditModeOn) View.VISIBLE else View.GONE
     itemView.remove_icon.visibility = if(isEditModeOn) View.VISIBLE else View.GONE
 }
}