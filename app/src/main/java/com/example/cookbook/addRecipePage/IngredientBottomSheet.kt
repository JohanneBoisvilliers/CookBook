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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.cookbook.R
import com.example.cookbook.models.Ingredient
import com.example.cookbook.models.IngredientDatabase
import com.example.cookbook.recipeDetailsPage.IngredientsListAdapter
import com.example.cookbook.recipesPage.RecipeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_ingredient_bottom_sheet.*

class IngredientBottomSheet : BottomSheetDialogFragment() {

    private lateinit var viewModel: RecipeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_ingredient_bottom_sheet, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        this.initViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenerOnCancelButton()
        listenerOnAddButton()
        listenerOnQuantityField()
    }

    // ---------------- INIT -------------------

    //get recipe view model for requests
    private fun initViewModel() {
        viewModel = ViewModelProviders.of(activity!!).get(RecipeViewModel::class.java)
    }

    // ---------------- LISTENERS -------------------

    //dismiss modal bottom sheet when pressing cancel button
    fun listenerOnCancelButton() {
        cancel_ingredient_button.setOnClickListener {
            dismiss()
        }
    }

    /*
    set a new ingredient depending to different inputs,
    and put this new ingredient in ingredient list in view model
    */
    private fun listenerOnAddButton() {
        save_ingredient_button.setOnClickListener {
            val ingredientDatabase = IngredientDatabase(name = "blabla")
            val ingredient = Ingredient(
                    quantity = viewModel.quantity.value!!,
                    unit = "gr",
                    ingredientDatabaseId = 1,
                    recipeId = 1,
                    ingredientId = 1,
                    ingredientDatabase = ingredientDatabase)
            viewModel.ingredientList.plus(ingredient)
        }
    }

    private fun listenerOnQuantityField(){
        qt_field.onTextChanged { viewModel.quantity.value = it.toInt() }
    }
    companion object {
        const val TAG = "ModalIngredientBottomSheet"
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