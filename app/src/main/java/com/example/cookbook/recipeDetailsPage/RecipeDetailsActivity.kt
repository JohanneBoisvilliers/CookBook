package com.example.cookbook.recipeDetailsPage

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.cookbook.R
import com.example.cookbook.addRecipePage.IngredientBottomSheet
import com.example.cookbook.addRecipePage.PhotoBottomSheet
import com.example.cookbook.addRecipePage.StepBottomSheet
import com.example.cookbook.injections.Injections
import com.example.cookbook.models.*
import com.example.cookbook.recipesPage.RecipeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_recipe_details.*
import kotlinx.android.synthetic.main.recyclerview_ingredient_item.view.*

class RecipeDetailsActivity : AppCompatActivity(), IngredientsListAdapter.Listener,StepListAdapter.Listener {

    private var viewModel: RecipeViewModel? = null
    private var recipeId: Long? = 0
    private var recipe: Recipe? = Recipe()
    private var viewPagerAdapter: PhotoViewPagerAdapter? = PhotoViewPagerAdapter(mutableListOf())
    private var ingredientAdapter: IngredientsListAdapter? = IngredientsListAdapter(mutableListOf(), false, this)
    private var stepAdapter: StepListAdapter? = StepListAdapter(mutableListOf(), false,this)
    private var ingredientList = mutableListOf<Ingredient>()

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_recipe_detail, menu)
        return true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.initRecipeViewModel()

        this.fetchRecipe()
        this.initViewPager()
        this.initIngredientRecyclerview()
        this.initStepRecyclerview()
        this.observerOnEditMode()
        this.observerOnIngredientList()
        this.observerOnStepList()
        this.observerOnPhotoList()
        this.listenerOnUpdateButton()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        R.id.action_modify -> {
            viewModel?.isUpdateModeOn?.value = !viewModel?.isUpdateModeOn?.value!!
            true
        }

        R.id.action_open_url -> {
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    // ---------------- INIT -------------------

    private fun initRecipeViewModel() {
        val viewModelFactory = Injections.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModel::class.java)
    }

    // settings of viewpager
    private fun initViewPager() {
        viewPager_recipe_details.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager_recipe_details.adapter = viewPagerAdapter

        TabLayoutMediator(rd_tab_layout, viewPager_recipe_details) { tab, position -> Unit }.attach()
    }

    private fun initIngredientRecyclerview() {
        ingredient_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ingredientAdapter
        }
    }

    private fun initStepRecyclerview() {
        recipe_step_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = stepAdapter
        }
    }

    // ---------------- OBSERVERS -------------------

    private fun observerOnEditMode() {
        viewModel?.isUpdateModeOn?.observe(this, Observer { isUpdateModeOn ->
            vp_add_photo.visibility = if (isUpdateModeOn) View.VISIBLE else View.GONE
            vp_del_photo.visibility = if (isUpdateModeOn) View.VISIBLE else View.GONE
            btn_add_ingredient.visibility = if (isUpdateModeOn) View.VISIBLE else View.GONE
            btn_add_step.visibility = if (isUpdateModeOn) View.VISIBLE else View.GONE
            btn_save.visibility = if (isUpdateModeOn) View.VISIBLE else View.GONE
            if (viewModel?.actualRecipe?.value != null) {
                updateUi(viewModel?.actualRecipe?.value!!, isUpdateModeOn)
            }
        })
    }

    private fun observerOnIngredientList() {
        viewModel?.ingredientList?.observe(this, Observer { list ->
            viewModel?.actualRecipe?.value!!.ingredientList.clear()
            if (viewModel?.actualRecipe?.value != null) {
                viewModel?.actualRecipe?.value!!.ingredientList.plusAssign(list.map { it.ingredientData }.toList())
                updateUi(viewModel?.actualRecipe?.value!!, viewModel?.isUpdateModeOn?.value!!)
            }
        })
    }

    private fun observerOnStepList() {
        viewModel?.stepList?.observe(this, Observer { list ->
//            updateStepList(list)
            if (viewModel?.actualRecipe?.value != null) {
                viewModel?.actualRecipe?.value!!.stepList = list
                updateUi(viewModel?.actualRecipe?.value!!, true)
            }
        })
    }

    private fun observerOnPhotoList() {
        viewModel?.photoList?.observe(this, Observer { list ->
            if (viewModel?.actualRecipe?.value != null) {
                viewModel?.actualRecipe?.value!!.photoList = list
                updateUi(viewModel?.actualRecipe?.value!!, true)
            }
        })
    }

    // ---------------- LISTENERS -------------------

    fun onClicked(view: View) {
        if (view is ImageButton) {
            when (view.id) {
                R.id.btn_add_ingredient -> {
                    viewModel?.isUpdateIconPressed?.value = false
                    showModalBottomSheet(IngredientBottomSheet(), IngredientBottomSheet.TAG)
                }
                R.id.btn_add_step -> {
                    showModalBottomSheet(StepBottomSheet(), StepBottomSheet.TAG)
                }
                R.id.vp_add_photo -> {
                    showModalBottomSheet(PhotoBottomSheet(), PhotoBottomSheet.TAG)
                }

            }
        }
    }

    private fun listenerOnUpdateButton() {
        btn_save.setOnClickListener {
            viewModel?.updateRecipe(viewModel?.actualRecipe?.value?.baseDataRecipe!!)
        }
    }

    // ---------------- ASYNC -------------------

    private fun fetchRecipe() {
        recipeId = intent.getLongExtra("recipe", 0)
        viewModel?.getRecipeWithIngredient(recipeId!!)?.observe(this, Observer { recipeFetch ->
            viewModel?.actualRecipe?.value = recipeFetch
        }
        )
    }

    // ---------------- UTILS -------------------

    // create ingredient list depending to ingredientData in recipe
    private fun initIngredientList(list: List<IngredientData>): List<Ingredient> {
        // init an ingredient list
        val ingredientList = mutableListOf<Ingredient>()
        // for each ingredientData in recipe, search the corresponding ingredientDatabase object
        list.forEach {
            val ingredientDatabase =
                    //find the first ingredientDatabase who has the same id than ingredientData's ingredientDatabaseId
                    viewModel?.ingredientDatabaseList?.firstOrNull { item -> item.ingredientDatabaseId == it.ingredientDatabaseId }
            // create an ingredient with this ingredientDatabase and this ingredientData and add it into final ingredient list
            ingredientList.add(Ingredient(it, ingredientDatabase!!))
        }
        return ingredientList
    }

    private fun updateUi(recipe: Recipe, isEditMode: Boolean = false) {
        this.recipe = recipe
        recipe_name.text = recipe.baseDataRecipe?.name
        viewPagerVisibility(recipe)
        this.viewPagerAdapter?.updatePhotoList(recipe.photoList)
        this.ingredientAdapter?.updateIngredientList(initIngredientList(recipe.ingredientList), isEditMode)
        this.stepAdapter?.updateStepList(recipe.stepList, isEditMode)
    }

    // set visibility of viewpager depending to recipe photo list size
    private fun viewPagerVisibility(recipe: Recipe) {
        val isListEmpty = recipe.photoList.size == 0
        viewPager_recipe_details.visibility = if (isListEmpty) View.INVISIBLE else View.VISIBLE
        empty_photo.visibility = if (isListEmpty) View.VISIBLE else View.INVISIBLE
    }

    private fun showModalBottomSheet(modal: BottomSheetDialogFragment, tag: String) {
        modal.show(supportFragmentManager, tag)
    }

    // set status bar state
    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    // ---------------- EXTENSIONS -------------------

    //extension for adding an item in a mutablelivedata list
    operator fun <T> MutableLiveData<MutableList<T>>.plusAssign(values: T) {
        val value = this.value ?: arrayListOf()
        value.add(values)
        this.value = value
    }

    // ---------------- CALLBACKS -------------------

    // callback to handle click on remove ingredient icon
    override fun onClickDeleteIngredientButton(ingredient: Ingredient) {
        viewModel?.ingredientList?.value?.remove(ingredient)
        viewModel?.ingredientList?.postValue(viewModel?.ingredientList?.value)
    }

    // callback to handle click on update ingredient icon
    override fun onClickUpdateIngredientButton(ingredient: Ingredient) {
        viewModel?.isUpdateIconPressed?.value = true
        val ingredientBottomSheet = IngredientBottomSheet()
        ingredientBottomSheet.arguments = bundleOf(
                "quantity" to ingredient.ingredientData.quantity.toString(),
                "name" to ingredient.ingredientDatabase.name,
                "unit" to ingredient.ingredientData.unit
        )
        showModalBottomSheet(ingredientBottomSheet, IngredientBottomSheet.TAG)
    }

    override fun onClickUpdateStep(step: Step) {
        viewModel?.isUpdateIconPressed?.value = true
        val stepBottomSheet = StepBottomSheet()
        stepBottomSheet.arguments = bundleOf(
                "description" to step.description
        )
        showModalBottomSheet(stepBottomSheet, StepBottomSheet.TAG)
    }

    override fun onClickRemoveStep(step: Step) {
        viewModel?.stepList?.value?.remove(step)
        viewModel?.stepList?.postValue(viewModel?.stepList?.value)
    }
}
