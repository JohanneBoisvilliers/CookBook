package com.example.cookbook.addRecipePage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.R
import com.example.cookbook.injections.Injections
import com.example.cookbook.models.BaseDataRecipe
import com.example.cookbook.models.Ingredient
import com.example.cookbook.models.IngredientDatabase
import com.example.cookbook.models.Recipe
import com.example.cookbook.recipeDetailsPage.IngredientsListAdapter
import com.example.cookbook.recipeDetailsPage.RecipeDetailsActivity
import com.example.cookbook.recipeDetailsPage.StepListAdapter
import kotlinx.android.synthetic.main.activity_add_recipe.*
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.time.LocalDateTime
import java.util.*

class AddRecipeActvity : AppCompatActivity() {
    private lateinit var mViewModel: AddRecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

//        initIngredientRecyclerview()
//        initSpinner()
        initViewModel()
        listenerOnRecipeName()
//        getIngredientList()
//        listenerOnIngredientQuantity()
//        listenerOnUnitSpinner()
        listenerOnFab()
    }

    // ---------------- INIT -------------------

    // configure ingredient recyclerview (ingredient list)
//    fun initIngredientRecyclerview() {
//        new_recipe_ingredient_list.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            adapter = ingredientAdapter
//        }
//    }
//    fun initStepRecyclerview() {
//        new_recipe_step_list.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            adapter = stepAdapter
//        }
//    }
    private fun initViewModel() {
        val viewModelFactory = Injections.provideViewModelFactory(this)
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddRecipeViewModel::class.java)
    }
    // configure the unit spinner for ingredient unit
//    private fun initSpinner(){
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter.createFromResource(
//                this,
//                R.array.unit_list,
//                android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            unit_spinner.adapter = adapter
//        }
//    }
    // set visibility for add ingredient card
//    private fun initAddIngredientButton(){
//        if (add_ingredient_fields.visibility == View.GONE) {
//            add_ingredient_fields.visibility = View.VISIBLE
//        }else{
//            add_ingredient_fields.visibility = View.GONE
//        }
//    }

//    private fun initIngredientNameField(ingredientList: List<String>){
//        // Initialize a new array adapter object
//        val adapter = ArrayAdapter<String>(
//                this, // Context
//                android.R.layout.simple_dropdown_item_1line, // Layout
//                ingredientList // Array
//        )
//        name_field.setAdapter(adapter)
//        // Auto complete threshold
//        // The minimum number of characters to type to show the drop down
//        name_field.threshold = 2
//
//        // Set an item click listener for auto complete text view
//        name_field.onItemClickListener = AdapterView.OnItemClickListener{
//            parent,view,position,id->
//            mViewModel.ingredientName.value = parent.getItemAtPosition(position).toString()
//        }
//
//    }

    // ---------------- LISTENERS -------------------
    fun onClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            if (checked) {
                recipe_url.visibility = View.VISIBLE
            } else {
                recipe_url.visibility = View.GONE
            }
        }
//        if (view is Button) {
//            when (view.id) {
//                R.id.save_ingredient_button -> {
//                    var ingredientDatabase = IngredientDatabase(name = "blabla")
//                    var ingredient = Ingredient(
//                            quantity = mViewModel.quantity.value!!,
//                            unit = mViewModel.unit.value!!,
//                            ingredientDatabaseId = 1,
//                            recipeId = 1,
//                            ingredientId = 1,
//                            ingredientDatabase = ingredientDatabase)
//                    mViewModel.ingredientList.plusAssign(ingredient)
//                    updateItemsList(mViewModel.ingredientList.value)
//                }
////                R.id.cancel_ingredient_button -> {
////                    if (add_ingredient_fields.visibility == View.VISIBLE) {
////                        add_ingredient_fields.visibility = View.GONE
////                    }
////                }
//            }
//
//        }
//        if(view is ImageButton){
//            when (view.id) {
//                R.id.add_ingredient_button -> {
//                    initAddIngredientButton()
//                }
//                R.id.add_photo_button ->{
//                    val modalBottomSheet = BottomSheetPhoto()
//                    modalBottomSheet.show(supportFragmentManager, BottomSheetPhoto.TAG)
//                }
//                R.id.add_step_button->{
//                    if (add_step_field.visibility == View.GONE) {
//                        add_step_field.visibility = View.VISIBLE
//                    }else{
//                        add_step_field.visibility = View.GONE
//                    }
//                }
//            }
//        }
    }

     private fun listenerOnRecipeName() {
        recipe_name_textfield.onTextChanged { mViewModel.recipeName.value = it }
    }

    // get value in quantity field
//    private fun listenerOnIngredientQuantity(){
//        qt_field.onTextChanged { mViewModel.quantity.value = it.toInt() }
//    }
//    // get value in unit spinner
//    private fun listenerOnUnitSpinner(){
//        unit_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//
//            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
//                // set unit value in viewmodel when choose an item in unit spinner
//                mViewModel.unit.value = parent.getItemAtPosition(pos).toString()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//            }
//        }
//
//        }
    private fun listenerOnFab() {
        fab_go_details.setOnClickListener {
            val isFieldFill = recipe_name_textfield.length() >= 3
            // check if user enter at least 3 characters on recipe name field
            when (isFieldFill) {
                false -> recipe_name_textfield.error = getString(R.string.error_recipe_name)
            }
            /*
            if field is filled then insert recipe in database with all inputs and
            send user on the recipe detail page
            */
            if (isFieldFill) {
                val newRecipe = BaseDataRecipe(
                        name = mViewModel.recipeName.value,
                        addDate = getCurrentDateTime().dateToString("dd/MM/yyyy"),
                        isAlreadyDone = false,
                        numberOfLike = 123,
                        recipeUrl = "test"
                )
                val newRecipeId = mViewModel.insertRecipe(newRecipe)

                val intent = Intent(this, RecipeDetailsActivity::class.java)
                intent.putExtra("recipe", newRecipeId)
                startActivity(intent)
            }
        }
    }
    // ---------------- UTILS -------------------

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
    //notify adapter that an item has changed
//    private fun updateItemsList(ingredientList: MutableList<Ingredient>?) {
//        mIngredientList.clear()
//        if (ingredientList != null) {
//            mIngredientList.addAll(ingredientList)
//        }
//        ingredientAdapter?.updateIngredientList(mIngredientList,false)
//    }

    // ---------------- ASYNC -------------------

//    private fun getIngredientList(){
//        mViewModel.ingredientListName.observe(this, Observer { list ->
//            initIngredientNameField(list)
//        })
//    }
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

fun Date.dateToString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}
