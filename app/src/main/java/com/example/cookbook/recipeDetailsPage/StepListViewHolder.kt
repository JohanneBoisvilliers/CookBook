package com.example.cookbook.recipeDetailsPage

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import com.example.cookbook.models.Step
import kotlinx.android.synthetic.main.recyclerview_ingredient_item.view.*
import kotlinx.android.synthetic.main.recyclerview_step_item.view.*

class StepListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @SuppressLint("StringFormatMatches")
    fun bind(step: Step, position:Int, isEditMode:Boolean) {
        itemView.step_title.text = itemView.context.getString(R.string.step,"${position + 1}")
        itemView.step_description.text = step.description
        itemView.update_step.visibility = if(isEditMode) View.VISIBLE else View.GONE
        itemView.remove_step.visibility = if(isEditMode) View.VISIBLE else View.GONE
    }
}