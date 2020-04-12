package com.example.cookbook.socialPage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import com.example.cookbook.models.Recipe
import kotlinx.android.synthetic.main.recyclerview_all_recipes_item.view.*
import java.util.*

class SharedRecipeAdapter constructor(list : MutableList<Map<String,Any>>) : RecyclerView.Adapter<SharedRecipeViewHolder>() {
    private var mContext: Context? = null
    private var mRandom: Random? = null
    private var mRecipeList:MutableList<Map<String,Any>> = list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedRecipeViewHolder {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(R.layout.recyclerview_social_item, parent, false)

        return SharedRecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SharedRecipeViewHolder, position: Int) {
        mRandom = Random()
        holder.updateRecipeCardUi(mRecipeList[position])
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