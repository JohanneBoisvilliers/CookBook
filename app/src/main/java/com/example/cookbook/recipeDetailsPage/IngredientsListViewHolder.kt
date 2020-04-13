package com.example.cookbook.recipeDetailsPage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.models.Ingredient
import kotlinx.android.synthetic.main.recyclerview_ingredient_item.view.*
import java.lang.ref.WeakReference

class IngredientsListViewHolder<T>(itemView:View):RecyclerView.ViewHolder(itemView){

    private var callbackWeakRef: WeakReference<IngredientsListAdapter.Listener>? = null

    fun bind(ingredient: T,isEditModeOn:Boolean,callback: IngredientsListAdapter.Listener){
        if (ingredient is Ingredient) {
            itemView.ingredient_quantity.text = ingredient.ingredientData.quantity.toString()
            itemView.ingredient_unit.text = ingredient.ingredientData.unit
            itemView.ingredient_name.text = ingredient.ingredientDatabase.name

            itemView.update_icon.visibility = if(isEditModeOn) View.VISIBLE else View.GONE
            itemView.remove_icon.visibility = if(isEditModeOn) View.VISIBLE else View.GONE
            callbackWeakRef = WeakReference(callback)

            itemView.remove_icon.setOnClickListener {
                callbackWeakRef!!.get()?.onClickDeleteIngredientButton(ingredient)
            }

            itemView.update_icon.setOnClickListener {
                callbackWeakRef!!.get()?.onClickUpdateIngredientButton(ingredient)
            }
        }

 }
}