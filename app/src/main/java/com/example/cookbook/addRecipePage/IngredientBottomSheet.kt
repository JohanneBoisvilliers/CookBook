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
import androidx.lifecycle.map
import com.example.cookbook.R
import com.example.cookbook.models.Ingredient
import com.example.cookbook.models.IngredientData
import com.example.cookbook.models.IngredientDatabase
import com.example.cookbook.recipesPage.RecipeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_ingredient_bottom_sheet.*


class IngredientBottomSheet : BottomSheetDialogFragment() {

    private lateinit var viewModel: RecipeViewModel
    private var ingredientQuantity: String? = null
    private var ingredientName: String? = null
    private var ingredientUnit: String? = null

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
        arguments?.let {
            fetchIngredientDataIfExist()
        }
        this.initSpinner()
        listenerOnCancelIngredientButton()
        listenerOnAddIngredientButton()
        listenerOnQuantityField()
        listenerOnUnitSpinner()
        initIngredientTextButton()
    }

    // ---------------- INIT -------------------

    //get recipe view model for requests
    private fun initViewModel(){
        viewModel = ViewModelProviders.of(requireActivity()).get(RecipeViewModel::class.java)
    }

    // configure the unit spinner for ingredient unit
    private fun initSpinner() {
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
            if (!isNullOrEmpty(ingredientUnit))
                unit_spinner.setSelection(adapter.getPosition(ingredientUnit))
        }
    }

    // set adapter on autocomplete text field for ingredient name
    private fun initIngredientNameField(ingredientList: List<IngredientDatabase>) {
        // Initialize a new array adapter object
        val adapter = ArrayAdapter<String>(
                activity!!, // Context
                android.R.layout.simple_dropdown_item_1line, // Layout
                ingredientList.map { it.name }.toList() // Array
        )
        name_field.setAdapter(adapter)
        if (!isNullOrEmpty(ingredientName)) {
            name_field.setText(ingredientName)
            viewModel.ingredientPicked.value = ingredientList.first { item -> item.name == ingredientName }
        }
        // Auto complete threshold
        // The minimum number of characters to type to show the drop down
        name_field.threshold = 2

        // Set an item click listener for auto complete text view
        name_field.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val ingredientPickedOnList =
                    ingredientList.firstOrNull { item -> item.name == parent.getItemAtPosition(position).toString() }
            viewModel.ingredientPicked.value = ingredientPickedOnList
        }

    }

    // check if user clicked on add ingredient button or update ingredient icon
    // and set button text (ADD or UPDATE) depending on previous action
    private fun initIngredientTextButton() {
        val isUpdatePressed = viewModel.isUpdateIconPressed.value!!
        save_ingredient_button.text = if (isUpdatePressed) getString(R.string.update_button_text) else getString(R.string.add_button_text)
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
            val isValid = !isAFieldEmpty() && isAnIngredientPicked()
            if (isValid) {
                if (viewModel.isUpdateIconPressed.value == true) {
                    fetchAndUpdateIngredient()
                } else {
                    createIngredient()
                }
                clearFieldsAfterInsert()
            }
        }
    }

    // get ingredient quantity from quantity field and save it into view model
    private fun listenerOnQuantityField() {
        qt_field.onTextChanged {
            val quantityValue: Int? = if (isNullOrEmpty(it)) {
                null
            } else {
                it.toInt()
            }
            viewModel.quantity.value = quantityValue
        }
    }

    // get value in unit spinner
    private fun listenerOnUnitSpinner() {
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
    private fun observerOnIngredientList() {
        viewModel.ingredientListPicked.observe(this, Observer { list ->
            initIngredientNameField(list)
        })
    }

    // ---------------- UTILS -------------------

    /*
    check is quantity and name fields are filled , if not, show an error message to
    tell the user to fill them all. return a boolean for listenerOnAddIngredientButton()
    If a field is empty return true : so we do not insert the ingredient in database
    */
    private fun isAFieldEmpty(): Boolean {
        val qtIsEmpty = qt_field.length() == 0
        val nameIsEmpty = name_field.length() == 0
        when (qtIsEmpty) {
            true -> qt_field.error = getString(R.string.ingredient_bottom_sheet_qt_error)
        }
        when (nameIsEmpty) {
            true -> name_field.error = getString(R.string.ingredient_bottom_sheet_name_error)
        }
        return qtIsEmpty or nameIsEmpty
    }

    /*
    check if user picked an ingredient into proposed ingredient list for ensure that
    is a valid ingredient
    */
    private fun isAnIngredientPicked(): Boolean {
        when (viewModel.ingredientPicked.value == null) {
            true -> {
                name_field.error = "pick a proposed ingredient"
                return false
            }
            false -> return true
        }
    }

    // check if string is null or empty
    private fun isNullOrEmpty(str: String?): Boolean {
        if (str != null && !str.trim().isEmpty())
            return false
        return true
    }

    /*
    after insert a new ingredient, clear all fields and set ingredient picked in viewmodel to null,
    to avoid that if a user picked an ingredient, and insert a second ingredient, viewmodel don't
    keep first ingredient name in memory
    */
    private fun clearFieldsAfterInsert() {
        qt_field.text.clear()
        name_field.text.clear()
        viewModel.ingredientPicked.value = null
    }

    // if we are in update case, fetch all information about the picked ingredient
    private fun fetchIngredientDataIfExist() {
        //get three arguments from bundle
        val name = arguments?.getString("name")
        val quantity = arguments?.getString("quantity")
        val unit = arguments?.getString("unit")
        //set local variable with bundle's data
        ingredientName = if (isNullOrEmpty(name)) "" else name
        ingredientQuantity = if (isNullOrEmpty(quantity)) "" else quantity
        ingredientUnit = if (isNullOrEmpty(unit)) "" else unit
        //init quantity field with quantity
        qt_field.setText(ingredientQuantity)
        // save data in viewmodel
        viewModel.ingredientPicked.value?.name = name!!
        viewModel.quantity.value = quantity?.toInt()
        viewModel.unit.value = unit!!
    }

    // if user want to update an ingredient
    private fun fetchAndUpdateIngredient() {
        // get the actual ingredient data to update
        val updatedIngredient =
                /*
                ingredientPicked can't be updated, so when user want to update an ingredient he
                can only update ingredient data(quantity and unit). So we have to search the first ingredientData which
                has the same id than ingredientPicked id
                */
                viewModel.ingredientList.value?.first { ingredient ->
                    // fetch ingredientData in viewModel ingredientList
                    ingredient.ingredientData.ingredientDatabaseId ==
                            viewModel.ingredientPicked.value?.ingredientDatabaseId
                }?.ingredientData
        // ingredientData is immutable so for update it, we have to make a copy
        val ingredientCopy =
                updatedIngredient?.copy(
                        quantity = viewModel.quantity.value!!,
                        unit = viewModel.unit.value!!
                )
        viewModel.ingredientList.replace(ingredientCopy!!)
        viewModel.ingredientList.postValue(viewModel.ingredientList.value)
        // request for update the ingredient
        viewModel.updateIngredients(ingredientCopy)
    }

    // create ingredientData and IngredientDatabase depending on user inputs
    private fun createIngredient() {
        val ingredientDetails = IngredientData(
                quantity = viewModel.quantity.value!!,
                unit = viewModel.unit.value!!,
                ingredientDatabaseId = viewModel.ingredientPicked.value!!.ingredientDatabaseId,
                recipeId = viewModel.actualRecipe.value?.baseDataRecipe?.baseRecipeId!!
        )
        val ingredientDatabase = IngredientDatabase(
                ingredientDatabaseId = viewModel.ingredientPicked.value!!.ingredientDatabaseId,
                name = viewModel.ingredientPicked.value!!.name
        )
        val ingredient = Ingredient(
                ingredientDetails,
                ingredientDatabase)
        viewModel.addIngredient(ingredient)
    }

    // ---------------- EXTENSIONS -------------------

    //extension for adding an item in a mutablelivedata list
    operator fun <T> MutableLiveData<MutableList<T>>.plusAssign(values: T) {
        val value = this.value ?: arrayListOf()
        value.add(values)
        this.value = value
    }

    // extension for replace an item in a mutableLiveData list with the one in parameter
    private fun MutableLiveData<MutableList<Ingredient>>.replace(ingredientData: IngredientData) {
        val index = this.value?.indexOf(this.value?.first { item ->
            item.ingredientData.ingredientDetailsId == ingredientData.ingredientDetailsId
        })

        this.value!![index!!].ingredientData = ingredientData
    }

    //extension for using lambda for onTextChanged function
    private fun EditText.onTextChanged(onTextChanged: (String) -> Unit) {
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