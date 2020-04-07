package com.example.cookbook.addRecipePage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.webkit.URLUtil
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.example.cookbook.R
import com.example.cookbook.injections.Injections
import com.example.cookbook.models.BaseDataRecipe
import com.example.cookbook.recipeDetailsPage.RecipeDetailsActivity
import kotlinx.android.synthetic.main.activity_add_recipe.*
import java.text.SimpleDateFormat
import java.util.*

class AddRecipeActvity : AppCompatActivity() {
    private lateinit var mViewModel: AddRecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        initViewModel()
        initCategorySpinner()
        listenerOnRecipeName()
        listenerOnUrlField()
        listenerOnFab()
    }

    // ---------------- INIT -------------------

    private fun initViewModel() {
        val viewModelFactory = Injections.provideViewModelFactory(this)
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddRecipeViewModel::class.java)
    }

    // configure the unit spinner for category
    private fun initCategorySpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                this,
                R.array.category_list,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            recipe_category.adapter = adapter
            recipe_category.setSelection(0)
        }
        listenerOnCategorySpinner()
    }

    // ---------------- LISTENERS -------------------
    fun onClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            if (checked) {
                recipe_url_container.visibility = View.VISIBLE
            } else {
                recipe_url_container.visibility = View.GONE
            }
        }
    }

     private fun listenerOnRecipeName() {
        recipe_name_textfield.onTextChanged { mViewModel.recipeName.value = it }
    }

    private fun listenerOnUrlField(){
        recipe_url_field.onTextChanged {
            mViewModel.recipeUrl.value = it
        }
    }

    private fun listenerOnFab() {
        fab_go_details.setOnClickListener {
            val isRecipeNameFilled = recipe_name_textfield.length() >= 3
            val isUrlFilled = if(!online_checkbox.isChecked){
                true
            }else online_checkbox.isChecked and URLUtil.isValidUrl(recipe_url_field.text.toString())

            // check if user enter at least 3 characters on recipe name field
            when (isRecipeNameFilled) {
                false -> recipe_name_textfield.error = getString(R.string.error_recipe_name)
            }

            when(isUrlFilled){
                false -> recipe_url_field.error = "enter valid url"
            }

            /*
            if field is filled then insert recipe in database with all inputs and
            send user on the recipe detail page
            */
            if (isRecipeNameFilled and isUrlFilled) {
                val newRecipe = BaseDataRecipe(
                        name = mViewModel.recipeName.value,
                        addDate = getCurrentDateTime().dateToString("dd/MM/yyyy"),
                        isAlreadyDone = false,
                        numberOfLike = 0,
                        recipeUrl = mViewModel.recipeUrl.value,
                        category = mViewModel.category.value
                )
                val newRecipeId = mViewModel.insertRecipe(newRecipe)

                val intent = Intent(this, RecipeDetailsActivity::class.java)
                intent.putExtra("recipe", newRecipeId)
                startActivity(intent)
            }
        }
    }
    // ---------------- UTILS -------------------

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
    // get value in category spinner
    private fun listenerOnCategorySpinner() {
        recipe_category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                // set unit value in viewmodel when choose an item in unit spinner
                mViewModel.category.value = parent.getItemAtPosition(pos).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

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

fun Date.dateToString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}
