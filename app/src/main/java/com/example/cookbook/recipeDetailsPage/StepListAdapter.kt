package com.example.cookbook.recipeDetailsPage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import com.example.cookbook.models.Ingredient
import com.example.cookbook.models.Step

class StepListAdapter constructor(list: MutableList<Step>):RecyclerView.Adapter<StepListViewHolder>(){

    private var mStepList:MutableList<Step> = list
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepListViewHolder {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(R.layout.recyclerview_step_item, parent, false)

        return StepListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mStepList.size
    }

    override fun onBindViewHolder(holder: StepListViewHolder, position: Int) {
        holder.bind(mStepList[position])
    }

    fun updateStepList(list:List<Step>){
        mStepList.clear()
        mStepList.addAll(list)
        notifyDataSetChanged()
    }
}
