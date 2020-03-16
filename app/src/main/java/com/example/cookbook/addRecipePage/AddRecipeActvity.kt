package com.example.cookbook.addRecipePage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.R
import com.example.cookbook.injections.Injections
import com.example.cookbook.models.Ingredient
import com.example.cookbook.models.IngredientDatabase
import com.example.cookbook.recipeDetailsPage.IngredientsListAdapter
import com.example.cookbook.recipeDetailsPage.StepListAdapter
import kotlinx.android.synthetic.main.activity_add_recipe.*

class AddRecipeActvity : AppCompatActivity() {
    private var mIngredientList = mutableListOf<Ingredient>()
    private var ingredientAdapter: IngredientsListAdapter? = IngredientsListAdapter(mutableListOf())
    private var stepAdapter: StepListAdapter? = StepListAdapter(mutableListOf())
    private lateinit var mViewModel: AddRecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        initIngredientRecyclerview()
        initSpinner()
        initViewModel()
        listenerOnIngredientQuantity()
        listenerOnUnitSpinner()
    }

    fun onClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            if (checked) {
                recipe_url.visibility = View.VISIBLE
            } else {
                recipe_url.visibility = View.GONE
            }
        }
        if (view is Button) {
            when (view.id) {
                R.id.add_ingredient_button -> {
                    if (add_ingredient_fields.visibility == View.GONE) {
                        add_ingredient_fields.visibility = View.VISIBLE
                    }else{
                        add_ingredient_fields.visibility = View.GONE
                    }
                }
                R.id.save_ingredient_button -> {
                    var ingredientDatabase = IngredientDatabase(name = "blabla")
                    var ingredient = Ingredient(
                            quantity = mViewModel.quantity.value!!,
                            unit = mViewModel.unit.value!!,
                            ingredientDatabaseId = 1,
                            recipeId = 1,
                            ingredientId = 1,
                            ingredientDatabase = ingredientDatabase)
                    mViewModel.ingredientList.plusAssign(ingredient)
                    updateItemsList(mViewModel.ingredientList.value)
                }
                R.id.cancel_ingredient_button -> {
                    if (add_ingredient_fields.visibility == View.VISIBLE) {
                        add_ingredient_fields.visibility = View.GONE
                    }
                }
                R.id.add_photo_button ->{
                    val modalBottomSheet = BottomSheetPhoto()
                    modalBottomSheet.show(supportFragmentManager, BottomSheetPhoto.TAG)
                }
                R.id.add_step_button->{
                    if (add_step_field.visibility == View.GONE) {
                        add_step_field.visibility = View.VISIBLE
                    }else{
                        add_step_field.visibility = View.GONE
                    }
                }
            }

        }
    }
    // configure ingredient recyclerview (ingredient list)
    fun initIngredientRecyclerview() {
        new_recipe_ingredient_list.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ingredientAdapter
        }
    }

    fun initStepRecyclerview() {
        new_recipe_step_list.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = stepAdapter
        }
    }

    private fun initViewModel() {
        val viewModelFactory = Injections.provideViewModelFactory(this)
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddRecipeViewModel::class.java)
    }
    // configure the unit spinner for ingredient unit
    private fun initSpinner(){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                this,
                R.array.unit_list,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            unit_spinner.adapter = adapter
        }
    }
    // get value in quantity field
    private fun listenerOnIngredientQuantity(){
        qt_field.onTextChanged { mViewModel.quantity.value = it.toInt() }
    }
    private fun listenerOnUnitSpinner(){
        unit_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                // An item was selected. You can retrieve the selected item using
                mViewModel.unit.value = parent.getItemAtPosition(pos).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        }
    //notify adapter that an item has changed
    private fun updateItemsList(ingredientList: MutableList<Ingredient>?) {
        mIngredientList.clear()
        if (ingredientList != null) {
            mIngredientList.addAll(ingredientList)
        }
        ingredientAdapter?.updateIngredientList(mIngredientList)
    }
}

// ---------------- EXTENSIONS -------------------

//extension for adding an item in a mutablelivedata list
operator fun <T> MutableLiveData<MutableList<T>>.plusAssign(values: T) {
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
