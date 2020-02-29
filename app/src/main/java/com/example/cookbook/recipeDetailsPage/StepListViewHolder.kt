package com.example.cookbook.recipeDetailsPage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.models.Step
import kotlinx.android.synthetic.main.recyclerview_step_item.view.*

class StepListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(step: Step) {
        itemView.step_title.text = step.title
        itemView.step_description.text = step.description
    }
}