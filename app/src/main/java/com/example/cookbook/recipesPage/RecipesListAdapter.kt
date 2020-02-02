package com.example.cookbook.recipesPage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import com.example.cookbook.models.Recipe
import java.util.*

class RecipesListAdapter constructor(list:MutableList<Recipe>) : RecyclerView.Adapter<RecipesListViewHolder>() {
    private var mContext: Context? = null
    private var mRandom: Random? = null
    private var mRecipeList:MutableList<Recipe> = list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesListViewHolder {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(R.layout.recyclerview_all_recipes_item, parent, false)
        return RecipesListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipesListViewHolder, position: Int) {
        mRandom = Random()
        holder.mPhotoContainer.layoutParams.height = getRandomHeight(550, 800)
        holder.updateRecipeCardUi(mRecipeList[position])
    }

    override fun getItemCount(): Int {
        return mRecipeList.size
    }

    private fun getRandomHeight(min: Int, max: Int): Int {
        return mRandom!!.nextInt(max - min + 1) + min
    }

    fun updateList(newList:List<Recipe>){
        mRecipeList.clear()
        mRecipeList.addAll(newList)
        notifyDataSetChanged()
    }
}