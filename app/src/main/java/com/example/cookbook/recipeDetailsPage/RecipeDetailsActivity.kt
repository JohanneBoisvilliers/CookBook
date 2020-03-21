package com.example.cookbook.recipeDetailsPage

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.cookbook.R
import com.example.cookbook.injections.Injections
import com.example.cookbook.models.Ingredient
import com.example.cookbook.models.IngredientDatabase
import com.example.cookbook.models.Recipe
import com.example.cookbook.recipesPage.RecipeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_recipe_details.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class RecipeDetailsActivity : AppCompatActivity() {

    private var mRecipeViewModel: RecipeViewModel? = null
    private var recipeId: Long? = 0
    private var recipe: Recipe? = Recipe()
    private var viewPagerAdapter: PhotoViewPagerAdapter? = PhotoViewPagerAdapter(mutableListOf())
    private var ingredientAdapter: IngredientsListAdapter? = IngredientsListAdapter(mutableListOf(),false)
    private var stepAdapter: StepListAdapter? = StepListAdapter(mutableListOf())
    var testList = mutableListOf<IngredientDatabase>()

    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
        this.initRecipeViewModel()

        this.fetchRecipe()
        this.initViewPager()
        this.initIngredientRecyclerview()
        this.initStepRecyclerview()
        this.listenerOnUpdateFab()
        this.observerOnEditMode()
    }

    // ---------------- INIT -------------------

    private fun initRecipeViewModel() {
        val viewModelFactory = Injections.provideViewModelFactory(this)
        mRecipeViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModel::class.java)
    }
    // settings of viewpager
    fun initViewPager() {
        viewPager_recipe_details.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager_recipe_details.adapter = viewPagerAdapter

        TabLayoutMediator(rd_tab_layout, viewPager_recipe_details) { tab, position -> Unit }.attach()
    }

    fun initIngredientRecyclerview() {
        ingredient_recycler_view.apply {
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

    // ---------------- LISTENERS -------------------

    private fun listenerOnUpdateFab(){
        fab_update_recipe.setOnClickListener{
            mRecipeViewModel?.isUpdateModeOn?.value = !mRecipeViewModel?.isUpdateModeOn?.value!!
        }
    }

    // ---------------- OBSERVERS -------------------

    private fun observerOnEditMode(){
        mRecipeViewModel?.isUpdateModeOn?.observe(this, Observer { isUpdateModeOn ->
            vp_add_photo.visibility = if(isUpdateModeOn) View.VISIBLE else View.GONE
            vp_del_photo.visibility = if(isUpdateModeOn) View.VISIBLE else View.GONE
            if(mRecipeViewModel?.actualRecipe?.value != null){
                updateUi(mRecipeViewModel?.actualRecipe?.value!!,isUpdateModeOn)
            }
        })
    }

    // ---------------- ASYNC -------------------

    private fun fetchRecipe() {
        recipeId = intent.getLongExtra("recipe", 0)
        mRecipeViewModel?.getSpecificRecipe(recipeId)?.observe(this, Observer { recipeFetch ->
            mRecipeViewModel?.actualRecipe?.value = recipeFetch
            runBlocking {
               launch { updateUi(recipeFetch) }
                initIngredientList(recipeFetch.ingredientList)
            }
        }
        )
    }

    private fun fetchIngredientsDatabase(ingredient: Ingredient){
         mRecipeViewModel?.getIngredientDatabase(ingredient.ingredientId)?.observe(this, Observer { ingredientReturned ->
            testList.add(ingredientReturned)
        })
    }

    private fun initIngredientList(list: List<Ingredient>) {
        list.forEach {  fetchIngredientsDatabase(it)  }
    }

    // ---------------- UTILS -------------------

    private fun updateUi(recipe: Recipe,isEditMode:Boolean = false) {
        this.recipe = recipe
        this.viewPagerAdapter?.updatePhotoList(recipe.photoList)
        this.ingredientAdapter?.updateIngredientList(recipe.ingredientList,isEditMode)
        this.stepAdapter?.updateStepList(recipe.stepList)
    }
}
