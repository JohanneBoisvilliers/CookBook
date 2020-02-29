package com.example.cookbook.addRecipePage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import com.example.cookbook.R
import kotlinx.android.synthetic.main.activity_add_recipe.*

class AddRecipeActvity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

    }

    fun onCheckBoxClicked(view: View){
        if(view is CheckBox){
            val checked: Boolean = view.isChecked
            if(checked){
                recipe_url.visibility = View.VISIBLE
            }else{
                recipe_url.visibility = View.GONE
            }
        }


    }
}
