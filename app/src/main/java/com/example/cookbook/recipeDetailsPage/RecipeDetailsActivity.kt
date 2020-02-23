package com.example.cookbook.recipeDetailsPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.example.cookbook.R
import com.example.cookbook.injections.Injections
import com.example.cookbook.models.Recipe
import com.example.cookbook.recipesPage.RecipeViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_recipe_details.*

class RecipeDetailsActivity : AppCompatActivity() {

    private var mRecipeViewModel: RecipeViewModel? = null
    private var recipeId : Long?=0
    private var recipe : Recipe?= Recipe()
    private var adapter:PhotoViewPagerAdapter?= PhotoViewPagerAdapter(mutableListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        this.configureRecipeViewModel()
        this.fetchRecipe()
        this.paramViewPager()

    }

    fun paramViewPager(){
        this.adapter=PhotoViewPagerAdapter(recipe!!.photoList)
        viewPager_recipe_details.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager_recipe_details.adapter = adapter

        TabLayoutMediator(rd_tab_layout, viewPager_recipe_details) { tab, position ->

        }.attach()
    }

    fun fetchRecipe(){
        recipeId = intent.getLongExtra("recipe",0)
        mRecipeViewModel?.getSpecificRecipe(recipeId)?.observe(this, Observer{recipeFetch -> this.updateListItems(recipeFetch)})
    }
    private fun configureRecipeViewModel() {
        val viewModelFactory = Injections.provideViewModelFactory(this)
        mRecipeViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModel::class.java)
    }

    private fun updateListItems(recipe:Recipe){
        this.recipe=recipe
        this.adapter?.updatePhotoList(recipe.photoList)
    }
}
