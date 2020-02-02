package com.example.cookbook.recipesPage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import java.util.*

class RecipesListAdapter : RecyclerView.Adapter<RecipesListViewHolder>() {
    private var mContext: Context? = null
    private var mRandom: Random? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesListViewHolder {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(R.layout.recyclerview_all_recipes_item, parent, false)
        return RecipesListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipesListViewHolder, position: Int) {
        mRandom = Random()
        holder.mPhotoContainer.layoutParams.height = getRandomHeight(550, 800)
    }

    override fun getItemCount(): Int {
        return 10
    }

    private fun getRandomHeight(min: Int, max: Int): Int {
        return mRandom!!.nextInt(max - min + 1) + min
    }
}