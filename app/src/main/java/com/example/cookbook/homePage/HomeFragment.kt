package com.example.cookbook.homePage


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.cookbook.R
import com.example.cookbook.models.Recipe
import com.example.cookbook.recipesPage.RecipeViewModel

import butterknife.BindView
import butterknife.ButterKnife

class HomeFragment : Fragment() {

    @BindView(R.id.main_recycler_view)
    internal var mMainRecyclerView: RecyclerView? = null

    private var mMainAdapter: MainRecyclerViewAdapter? = null
    private var mRecipeViewModel: RecipeViewModel? = null

    private val mFinalEmbeddedList: MutableList<List<Recipe>>? = null
    private val mLatestRecipes: List<Recipe>? = null
    private val mFavoriteRecipes: List<Recipe>? = null
    private val mNotDoneRecipes: List<Recipe>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        ButterKnife.bind(this, view)

        this.configureMainRecyclerView()
        this.configureViewRecipeViewModel()
        return view
    }

    // ----------------------------------- UTILS -----------------------------------
    private fun configureMainRecyclerView() {
        this.mMainAdapter = MainRecyclerViewAdapter(mFinalEmbeddedList)
        this.mMainRecyclerView?.adapter = this.mMainAdapter
        this.mMainRecyclerView?.layoutManager = LinearLayoutManager(context)
    }

    private fun configureViewRecipeViewModel() {
        mRecipeViewModel = ViewModelProviders.of(activity!!).get(RecipeViewModel::class.java)
    }

    private fun updateItemList(embeddedList: List<List<Recipe>>) {
        this.mFinalEmbeddedList?.clear()
        this.mFinalEmbeddedList?.addAll(embeddedList)
        this.mMainAdapter!!.notifyItemChanged(mFinalEmbeddedList)
    }

    // ----------------------------------- ASYNC -----------------------------------

    private fun getRecipe() {
    }

    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

}
