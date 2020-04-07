package com.example.cookbook.recipeDetailsPage

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
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
import com.example.cookbook.onlineREcipe.RecipeOnlineActivity
import com.example.cookbook.recipesPage.RecipeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_recipe_details.*

class RecipeDetailsActivity : AppCompatActivity(), IngredientsListAdapter.Listener, StepListAdapter.Listener {

    private var viewModel: RecipeViewModel? = null
    private var recipeId: Long? = 0
    private var recipe: Recipe? = Recipe()
    private var viewPagerAdapter: PhotoViewPagerAdapter? = PhotoViewPagerAdapter(mutableListOf())
    private var ingredientAdapter: IngredientsListAdapter? = IngredientsListAdapter(mutableListOf(), false, this)
    private var stepAdapter: StepListAdapter? = StepListAdapter(mutableListOf(), false, this)
    private lateinit var menu: Menu

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
        this.menu = menu!!
        invalidateOptionsMenu()
        menu.findItem(R.id.action_open_url).isEnabled = !viewModel?.isNotOnline!!
        menu.findItem(R.id.action_open_url).isVisible = !viewModel?.isNotOnline!!
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
        this.listenerOnRecipeName()
        this.listenerOnUrlField()
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

        R.id.action_open_url ->
        {
            val intent = Intent(this, RecipeOnlineActivity::class.java)
            intent.putExtra("url", viewModel?.actualRecipe?.value?.baseDataRecipe?.recipeUrl)
            startActivity(intent)
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
        viewPager_recipe_details.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel?.photoSelected?.value = position
            }
        })

        TabLayoutMediator(rd_tab_layout, viewPager_recipe_details) { _, _ -> Unit }.attach()
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

    /*
   depending on if we are on update mode or not, set update button visibility for recipe name,
   and set edit text focusable to edit field
   */
    private fun initRecipeNameField(isUpdateModeOn: Boolean) {
        btn_update_recipe_name.visibility = if (isUpdateModeOn) View.VISIBLE else View.GONE
        recipe_name.isFocusableInTouchMode = isUpdateModeOn
        recipe_name.isFocusable = isUpdateModeOn
    }

    /*
    depending on if we are on update mode or not, set url field visibility,
    and set edit text focusable to edit field
    */
    private fun initUrlField(isUpdateModeOn: Boolean) {
        url_icon.visibility = if (isUpdateModeOn) View.VISIBLE else View.GONE
        url_field.visibility = if (isUpdateModeOn) View.VISIBLE else View.GONE
        if (url_field.visibility == View.VISIBLE){
            url_field.setText(viewModel?.actualRecipe?.value?.baseDataRecipe?.recipeUrl)
        }
        btn_update_url.visibility = if (isUpdateModeOn) View.VISIBLE else View.GONE
        url_field.isFocusableInTouchMode = isUpdateModeOn
        url_field.isFocusable = isUpdateModeOn
        url_field.isLongClickable = isUpdateModeOn
    }

    // ---------------- OBSERVERS -------------------

    private fun observerOnEditMode() {
        viewModel?.isUpdateModeOn?.observe(this, Observer { isUpdateModeOn ->
            vp_add_photo.visibility = if (isUpdateModeOn) View.VISIBLE else View.GONE
            vp_del_photo.visibility = if (isUpdateModeOn) View.VISIBLE else View.GONE
            btn_add_ingredient.visibility = if (isUpdateModeOn) View.VISIBLE else View.GONE
            btn_add_step.visibility = if (isUpdateModeOn) View.VISIBLE else View.GONE
            initRecipeNameField(isUpdateModeOn)
            initUrlField(isUpdateModeOn)
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
                updateIngredientList(list, viewModel?.isUpdateModeOn?.value!!)
            }
        })
    }

    private fun observerOnStepList() {
        viewModel?.stepList?.observe(this, Observer { list ->
            if (viewModel?.actualRecipe?.value != null) {
                updateStepList(list, viewModel?.isUpdateModeOn?.value!!)
            }
        })
    }

    private fun observerOnPhotoList() {
        viewModel?.photoList?.observe(this, Observer { list ->
            if (viewModel?.actualRecipe?.value != null) {
                viewModel?.actualRecipe?.value!!.photoList = list
                updatePhotoList(list)
            }
        })
    }

    // ---------------- LISTENERS -------------------

    fun onClicked(view: View) {
        if (view is ImageButton) {
            when (view.id) {
                R.id.btn_update_recipe_name -> {
                    //get actual recipe
                    val recipe = viewModel?.actualRecipe?.value?.baseDataRecipe
                    //hide keyboard and unset focus on edit text
                    recipe_name.hideKeyboard()
                    recipe_name.clearFocus()
                    //update recipe name into database
                    viewModel?.updateRecipeName(
                            recipe?.baseRecipeId!!,
                            recipe.name!!
                    )
                }
                R.id.btn_update_url -> {
                    //get actual recipe
                    val recipe = viewModel?.actualRecipe?.value?.baseDataRecipe
                    //hide keyboard and unset focus on edit text
                    url_field.hideKeyboard()
                    url_field.clearFocus()
                    //update recipe url into database
                    viewModel?.updateRecipeUrl(
                            recipe?.baseRecipeId!!,
                            recipe.recipeUrl!!
                    )
                }
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
                R.id.vp_del_photo -> {
                    val photo =
                            viewModel?.photoList?.value!![viewModel?.photoSelected?.value!!]
                    viewModel?.deletePhoto(photo)
                }
            }
        }
    }

    // save the new recipe name depending to user input and save it into view model
    private fun listenerOnRecipeName() {
        recipe_name.onTextChanged {
            val recipe = viewModel?.actualRecipe?.value?.baseDataRecipe
            val recipeCopy = recipe?.copy(name = it)
            viewModel?.actualRecipe?.value?.baseDataRecipe = recipeCopy!!
        }
    }

    private fun listenerOnUrlField(){
        url_field.onTextChanged {
            val recipe = viewModel?.actualRecipe?.value?.baseDataRecipe
            val recipeCopy = recipe?.copy(recipeUrl = it)
            viewModel?.actualRecipe?.value?.baseDataRecipe = recipeCopy!!
        }
    }

    // ---------------- ASYNC -------------------

    private fun fetchRecipe() {
        recipeId = intent.getLongExtra("recipe", 0)
        viewModel?.getRecipeWithIngredient(recipeId!!)?.observe(this, Observer { recipeFetch ->
            viewModel?.actualRecipe?.value = recipeFetch
            recipe_name.setText(recipeFetch.baseDataRecipe?.name)
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

    // update all the recipe detail page (specially for edit mode activation)
    private fun updateUi(recipe: Recipe, isEditMode: Boolean = false) {
        this.recipe = recipe
        viewPagerVisibility(recipe.photoList)
        this.viewPagerAdapter?.updatePhotoList(recipe.photoList)
        this.ingredientAdapter?.updateIngredientList(initIngredientList(recipe.ingredientList), isEditMode)
        this.stepAdapter?.updateStepList(recipe.stepList, isEditMode)
    }

    // update only the ingredient list
    private fun updateIngredientList(ingredientList: MutableList<Ingredient>, isEditMode: Boolean) {
        this.ingredientAdapter?.updateIngredientList(ingredientList, isEditMode)
    }

    //update only the step list
    private fun updateStepList(stepList: MutableList<Step>, isEditMode: Boolean) {
        this.stepAdapter?.updateStepList(stepList, isEditMode)
    }

    // update only the photo list
    private fun updatePhotoList(photoList: MutableList<Photo>) {
        viewPagerVisibility(photoList)
        this.viewPagerAdapter?.updatePhotoList(photoList)
    }

    // set visibility of viewpager depending to recipe photo list size
    private fun viewPagerVisibility(photoList: MutableList<Photo>) {
        val isListEmpty = photoList.size == 0
        viewPager_recipe_details.visibility = if (isListEmpty) View.INVISIBLE else View.VISIBLE
        empty_photo.visibility = if (isListEmpty) View.VISIBLE else View.INVISIBLE
    }

    // show a modal depending on what button user clicked(add ingredient, add step, add photo)
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
        viewModel?.ingredientList?.remove(ingredient)
        viewModel?.ingredientList?.postValue(viewModel?.ingredientList?.value)
        viewModel?.deleteIngredients(ingredient.ingredientData)
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
                "description" to step.description,
                "id" to step.id
        )
        showModalBottomSheet(stepBottomSheet, StepBottomSheet.TAG)
    }

    override fun onClickRemoveStep(step: Step) {
        viewModel?.stepList?.value?.remove(step)
        viewModel?.stepList?.postValue(viewModel?.stepList?.value)
        viewModel?.deleteStep(step)
    }

    // ---------------- EXTENSIONS -------------------

    // extension for remove an item in a mutableLiveData list
    private fun MutableLiveData<MutableList<Ingredient>>.remove(ingredient: Ingredient) {
        // find position of ingredient to remove
        val index = this.value?.indexOf(this.value?.first { item ->
            item.ingredientDatabase.ingredientDatabaseId == ingredient.ingredientDatabase.ingredientDatabaseId
        })
        // remove ingredient
        this.value?.removeAt(index!!)
    }

    // extension for hiding keyboard from a view
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
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
