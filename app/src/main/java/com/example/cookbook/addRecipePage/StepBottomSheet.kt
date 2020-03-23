package com.example.cookbook.addRecipePage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.example.cookbook.R
import com.example.cookbook.models.Step
import com.example.cookbook.recipesPage.RecipeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_recipe_details.*
import kotlinx.android.synthetic.main.fragment_step_bottom_sheet.*

class StepBottomSheet : BottomSheetDialogFragment() {

    private lateinit var viewModel: RecipeViewModel

    companion object {
        const val TAG = "ModalStepBottomSheet"
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_step_bottom_sheet, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        initViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenerOnStepField()
        listenerOnAddStepButton()
        listenerOnCancelButton()
    }

    // ---------------- INIT -------------------

    //get recipe view model for requests
    private fun initViewModel() {
        viewModel = ViewModelProviders.of(activity!!).get(RecipeViewModel::class.java)
    }

    // ---------------- LISTENERS -------------------

    private fun listenerOnStepField(){
        step_field.onTextChanged { viewModel.newStepText.value = it }
    }

    private fun listenerOnAddStepButton(){
        save_step_button.setOnClickListener {
            val stepToAdd = Step(description = viewModel.newStepText.value!!,recipeId = 1)
            viewModel.stepList.plus(stepToAdd)
        }
    }
    // close step bottom sheet
    private fun listenerOnCancelButton(){
        cancel_step_button.setOnClickListener {
            dismiss()
        }
    }

    // ---------------- EXTENSIONS -------------------

    //extension for adding an item in a mutablelivedata list
    operator fun <T> MutableLiveData<MutableList<T>>.plus(values: T) {
        val value = this.value ?: arrayListOf()
        value.add(values)
        this.value = value
    }

    //extension for using lambda for onTextChanged function
    fun EditText.onTextChanged(onTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onTextChanged.invoke(p0.toString())
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })
    }

}