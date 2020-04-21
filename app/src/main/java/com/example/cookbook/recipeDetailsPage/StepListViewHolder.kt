package com.example.cookbook.recipeDetailsPage

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import com.example.cookbook.models.Step
import kotlinx.android.synthetic.main.recyclerview_ingredient_item.view.*
import kotlinx.android.synthetic.main.recyclerview_step_item.view.*
import java.lang.ref.WeakReference

class StepListViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var callbackWeakRef: WeakReference<StepListAdapter.Listener>? = null

    @SuppressLint("StringFormatMatches")
    fun bind(step: T, position:Int, isEditMode:Boolean,callback:StepListAdapter.Listener) {
        if (step is Step) {
            itemView.step_title.text = itemView.context.getString(R.string.step,"${position + 1}")
            itemView.step_description.text = step.description
            itemView.update_step.visibility = if(isEditMode) View.VISIBLE else View.GONE
            itemView.remove_step.visibility = if(isEditMode) View.VISIBLE else View.GONE

            callbackWeakRef = WeakReference(callback)

            itemView.remove_step.setOnClickListener {
                callbackWeakRef!!.get()?.onClickRemoveStep(step)
            }

            itemView.update_step.setOnClickListener {
                callbackWeakRef!!.get()?.onClickUpdateStep(step)
            }
        }else{
            itemView.step_title.text = itemView.context.getString(R.string.step,"${position + 1}")
            itemView.step_description.text = step as String
            itemView.update_step.visibility = if(isEditMode) View.VISIBLE else View.GONE
            itemView.remove_step.visibility = if(isEditMode) View.VISIBLE else View.GONE
        }
    }
}