package com.example.cookbook.recipeDetailsPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import com.example.cookbook.models.Ingredient

class IngredientsListAdapter constructor(list:MutableList<Ingredient>,isEditMode:Boolean,callback:Listener) : RecyclerView.Adapter<IngredientsListViewHolder>(){
    private var mIngredientList:MutableList<Ingredient> = list
    private var mContext: Context? = null
    private var mIsEditModeOn = isEditMode
    private var callback = callback

    interface Listener {
        fun onClickDeleteIngredientButton(ingredient: Ingredient)
        fun onClickUpdateIngredientButton(ingredient: Ingredient)
    }

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
        holder.bind(mIngredientList[position],mIsEditModeOn,callback)
    }

    fun updateIngredientList(list:List<Ingredient>,isEditMode: Boolean = true){
        mIsEditModeOn=isEditMode
        mIngredientList.clear()
        mIngredientList.addAll(list)
        notifyDataSetChanged()
    }
}