package com.example.cookbook.addRecipePage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.cookbook.R
import com.example.cookbook.models.Ingredient
import com.example.cookbook.models.IngredientDatabase
import com.example.cookbook.recipesPage.RecipeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_ingredient_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_step_bottom_sheet.*

class IngredientBottomSheet : BottomSheetDialogFragment() {

    private lateinit var viewModel: RecipeViewModel
    // use for instantiate fragment
    companion object {
        const val TAG = "ModalIngredientBottomSheet"
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_ingredient_bottom_sheet, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        this.initViewModel()
        observerOnIngredientList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.initSpinner()
        listenerOnCancelIngredientButton()
        listenerOnAddIngredientButton()
        listenerOnQuantityField()
        listenerOnUnitSpinner()
    }

    // ---------------- INIT -------------------

    //get recipe view model for requests
    private fun initViewModel() {
        viewModel = ViewModelProviders.of(activity!!).get(RecipeViewModel::class.java)
    }
    // configure the unit spinner for ingredient unit
    private fun initSpinner(){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                activity!!,
                R.array.unit_list,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            unit_spinner.adapter = adapter
        }
    }
    // set adapter on autocomplete text field for ingredient name
    private fun initIngredientNameField(ingredientList: List<String>) {
        // Initialize a new array adapter object
        val adapter = ArrayAdapter<String>(
                activity!!, // Context
                android.R.layout.simple_dropdown_item_1line, // Layout
                ingredientList // Array
        )
        name_field.setAdapter(adapter)
        // Auto complete threshold
        // The minimum number of characters to type to show the drop down
        name_field.threshold = 2

        // Set an item click listener for auto complete text view
        name_field.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            viewModel.ingredientName.value = parent.getItemAtPosition(position).toString()
        }

    }

    // ---------------- LISTENERS -------------------

    //dismiss modal bottom sheet when pressing cancel button
    private fun listenerOnCancelIngredientButton() {
        cancel_ingredient_button.setOnClickListener {
            dismiss()
        }
    }
    /*
    set a new ingredient depending to different inputs,
    and put this new ingredient in ingredient list in view model
    */
    private fun listenerOnAddIngredientButton() {
        save_ingredient_button.setOnClickListener {
            val ingredientDatabase = IngredientDatabase(name = viewModel.ingredientName.value!!)
            val ingredient = Ingredient(
                    quantity = viewModel.quantity.value!!,
                    unit = viewModel.unit.value!!,
                    ingredientDatabaseId = 1,
                    recipeId = viewModel.actualRecipe.value?.baseDataRecipe?.baseRecipeId!!,
                    ingredientDatabase = ingredientDatabase)
            viewModel.ingredientList.plus(ingredient)
        }
    }
    // get ingredient quantity from quantity field and save it into view model
    private fun listenerOnQuantityField(){
        qt_field.onTextChanged { viewModel.quantity.value = it.toInt() }
    }
    // get value in unit spinner
    private fun listenerOnUnitSpinner(){
        unit_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                // set unit value in viewmodel when choose an item in unit spinner
                viewModel.unit.value = parent.getItemAtPosition(pos).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
}

    // ---------------- OBSERVER -------------------

    // get ingredient list from view model and set auto complete text field adapter
    private fun observerOnIngredientList(){
        viewModel.ingredientListName.observe(this, Observer { list ->
            initIngredientNameField(list)
        })
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