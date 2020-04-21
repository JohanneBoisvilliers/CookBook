package com.example.cookbook.socialPage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import com.example.cookbook.models.Recipe
import com.example.cookbook.recipeDetailsPage.IngredientsListAdapter
import kotlinx.android.synthetic.main.recyclerview_all_recipes_item.view.*
import java.util.*

class SharedRecipeAdapter constructor(list : MutableList<Map<String,Any>>) : RecyclerView.Adapter<SharedRecipeViewHolder>() {
    private var mContext: Context? = null
    private var mRecipeList:MutableList<Map<String,Any>> = list
    private var callback:Listener?=null

    interface Listener{
        fun onItemClick(position: Int)
        fun onClickLikeButton(recipe:Map<String,Any>)
    }

    fun setOnItemClickListener(listener: Listener){
        this.callback = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedRecipeViewHolder {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(R.layout.recyclerview_social_item, parent, false)

        return SharedRecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SharedRecipeViewHolder, position: Int) {
        holder.updateRecipeCardUi(mRecipeList[position],callback!!)
    }

    override fun getItemCount(): Int {
        return mRecipeList.size
    }

    fun updateList(newList:List<Map<String,Any>>){
        mRecipeList.clear()
        mRecipeList.addAll(newList)
        notifyDataSetChanged()
    }
}