package com.example.cookbook.recipesPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cookbook.R
import kotlinx.android.synthetic.main.fragment_recipes.*

class RecipesFragment : Fragment() {

    private var mListAdapter: RecipesListAdapter = RecipesListAdapter()

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
        configureRecyclerView()
    }
    //configure recycler view : set layout manager and adapter
    private fun configureRecyclerView() {
        all_recipes_recyclerview.apply {
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            adapter = mListAdapter
        }
    }
    //companion for instantiate fragment
    companion object {
        fun newInstance(): RecipesFragment = RecipesFragment()
    }
}