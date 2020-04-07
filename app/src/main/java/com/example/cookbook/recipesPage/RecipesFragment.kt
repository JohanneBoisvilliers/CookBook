package com.example.cookbook.recipesPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cookbook.R
import com.example.cookbook.addRecipePage.AddRecipeActvity
import com.example.cookbook.models.Recipe
import com.example.cookbook.recipeDetailsPage.RecipeDetailsActivity
import com.example.cookbook.utils.RecyclerItemClickListenr
import kotlinx.android.synthetic.main.fragment_recipes.*

class RecipesFragment : Fragment() {

    private var mRecipesList = mutableListOf<Recipe>()
    private var mListAdapter: RecipesListAdapter = RecipesListAdapter(mRecipesList)
    lateinit var mRecipeViewModel: RecipeViewModel
    private val RECIPE_RESULT:Int = 999


    //--------------LIFE CYCLE--------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
    //inflate fragment recipe
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_recipes, container, false)
    //configure fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecipeViewModel()
        configureRecyclerView()
        handleFabClick()
        getRecipes()
    }

    //--------------UTILS--------------

    //configure recycler view : set layout manager and adapter
    private fun configureRecyclerView() {
        all_recipes_recyclerview.apply {
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            adapter = mListAdapter
        }
        all_recipes_recyclerview.addOnItemTouchListener(RecyclerItemClickListenr(requireContext(), all_recipes_recyclerview, object : RecyclerItemClickListenr.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(context, RecipeDetailsActivity::class.java)
                intent.putExtra("recipe",mRecipesList[position].baseDataRecipe?.baseRecipeId)

                startActivity(intent)
            }
            override fun onItemLongClick(view: View?, position: Int) {
                print(position)
            }
        }))
    }
    //get recipe view model for requests
    private fun configureRecipeViewModel(){
        mRecipeViewModel = ViewModelProviders.of(activity!!).get(RecipeViewModel::class.java)
    }
    private fun updateItemsList(recipesList:List<Recipe>){
        mRecipesList.clear()
        mRecipesList.addAll(recipesList)
        mListAdapter.updateList(recipesList)
    }
    private fun handleFabClick(){
        val intent = Intent(context, AddRecipeActvity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

        fab_search_recipe.setOnClickListener { startActivity(intent) }
    }
    //companion for instantiate fragment
    companion object {
        fun newInstance(): RecipesFragment = RecipesFragment()
    }

    //--------------ASYNC--------------
    private fun getRecipes() {
        mRecipeViewModel.recipes.observe(this, Observer { list -> updateItemsList(list) })
    }
}