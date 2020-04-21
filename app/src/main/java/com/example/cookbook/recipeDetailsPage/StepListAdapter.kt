package com.example.cookbook.recipeDetailsPage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import com.example.cookbook.models.Ingredient
import com.example.cookbook.models.Step

class StepListAdapter <T>constructor(list: MutableList<T>,isEditMode:Boolean,callback:Listener):RecyclerView.Adapter<StepListViewHolder<T>>(){

    private var mStepList:MutableList<T> = list
    private var mContext: Context? = null
    private var mIsEditMode = isEditMode
    private var callback = callback

    interface Listener {
        fun onClickUpdateStep(step:Step)
        fun onClickRemoveStep(step:Step)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepListViewHolder<T> {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(R.layout.recyclerview_step_item, parent, false)

        return StepListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mStepList.size
    }

    override fun onBindViewHolder(holder: StepListViewHolder<T>, position: Int) {
        holder.bind(mStepList[position],position,mIsEditMode,callback)
    }

    fun updateStepList(list:List<T>,isEditMode: Boolean){
        mIsEditMode = isEditMode
        mStepList.clear()
        mStepList.addAll(list)
        notifyDataSetChanged()
    }
}
