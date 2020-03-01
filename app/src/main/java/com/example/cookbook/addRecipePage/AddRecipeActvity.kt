package com.example.cookbook.addRecipePage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.R
import com.example.cookbook.recipeDetailsPage.IngredientsListAdapter
import com.example.cookbook.recipeDetailsPage.StepListAdapter
import kotlinx.android.synthetic.main.activity_add_recipe.*
import kotlinx.android.synthetic.main.activity_recipe_details.*

class AddRecipeActvity : AppCompatActivity() {
    private var ingredientAdapter: IngredientsListAdapter? = IngredientsListAdapter(mutableListOf())
    private var stepAdapter: StepListAdapter?= StepListAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        paramIngredientRecyclerview()
    }

    fun onClicked(view: View){
        if(view is CheckBox){
            val checked: Boolean = view.isChecked
            if(checked){
                recipe_url.visibility = View.VISIBLE
            }else{
                recipe_url.visibility = View.GONE
            }
        }
        if(view is Button){
            when(view.id){
                R.id.add_ingredient_button ->{
                    if(add_ingredient_fields.visibility == View.GONE){
                        add_ingredient_fields.visibility = View.VISIBLE
                    }
                }
            }

        }
    }
    fun paramIngredientRecyclerview() {
        new_recipe_ingredient_list.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ingredientAdapter
        }
    }

    fun paramStepRecyclerview() {
        recipe_step_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = stepAdapter
        }
    }


}
