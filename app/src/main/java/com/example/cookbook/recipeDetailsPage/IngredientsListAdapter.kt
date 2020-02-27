package com.example.cookbook.recipeDetailsPage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import com.example.cookbook.models.Ingredient
import com.example.cookbook.recipesPage.RecipesListViewHolder

class IngredientsListAdapter constructor(list:MutableList<Ingredient>) : RecyclerView.Adapter<IngredientsListViewHolder>(){
    private var mIngredientList:MutableList<Ingredient> = list
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsListViewHolder {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(R.layout.recyclerview_ingredient_item, parent, false)

        return IngredientsListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mIngredientList.size
    }

    override fun onBindViewHolder(holder: IngredientsListViewHolder, position: Int) {
        holder.bind(mIngredientList[position])
    }

    fun updateList(list:List<Ingredient>){
        mIngredientList.clear()
        mIngredientList.addAll(list)
        notifyDataSetChanged()
    }
}