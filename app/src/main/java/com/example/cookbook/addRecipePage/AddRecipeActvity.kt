package com.example.cookbook.addRecipePage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.R
import com.example.cookbook.injections.Injections
import com.example.cookbook.models.Ingredient
import com.example.cookbook.models.IngredientDatabase
import com.example.cookbook.models.IngredientDetails
import com.example.cookbook.models.Recipe
import com.example.cookbook.recipeDetailsPage.IngredientsListAdapter
import com.example.cookbook.recipeDetailsPage.StepListAdapter
import kotlinx.android.synthetic.main.activity_add_recipe.*
import kotlinx.android.synthetic.main.activity_recipe_details.*

class AddRecipeActvity : AppCompatActivity() {
    private var mIngredientList = mutableListOf<Ingredient>()
    private var ingredientAdapter: IngredientsListAdapter? = IngredientsListAdapter(mutableListOf())
    private var stepAdapter: StepListAdapter? = StepListAdapter(mutableListOf())
    private lateinit var mViewModel: AddRecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        initIngredientRecyclerview()
        configureViewModel()
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
                R.id.save_button -> {
                    //var ingredientDatabase = IngredientDatabase(name = "blabla")
                    //var ingredientDetails = IngredientDetails(1, 1, mViewModel.unit.value.orEmpty(), 1)
                    //var ingredient = Ingredient(ingredientDatabase = ingredientDatabase, ingredientDetails = ingredientDetails)
                    //mViewModel.ingredientList.plusAssign(ingredient)
                    //updateItemsList(mViewModel.ingredientList.value)
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
            }

        }
    }

    fun initIngredientRecyclerview() {
        new_recipe_ingredient_list.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ingredientAdapter
        }
    }

    fun initStepRecyclerview() {
        recipe_step_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = stepAdapter
        }
    }

    private fun configureViewModel() {
        val viewModelFactory = Injections.provideViewModelFactory(this)
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddRecipeViewModel::class.java)
    }

    private fun updateItemsList(ingredientList: MutableList<Ingredient>?) {
        mIngredientList.clear()
        if (ingredientList != null) {
            mIngredientList.addAll(ingredientList)
        }
        ingredientAdapter?.updateIngredientList(mIngredientList)
    }
}

operator fun <T> MutableLiveData<MutableList<T>>.plusAssign(values: T) {
    val value = this.value ?: arrayListOf()
    value.add(values)
    this.value = value
}
