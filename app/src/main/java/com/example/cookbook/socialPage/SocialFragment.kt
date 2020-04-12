package com.example.cookbook.socialPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cookbook.R
import com.example.cookbook.injections.Injections
import com.example.cookbook.models.Recipe
import com.example.cookbook.recipeDetailsPage.RecipeDetailsActivity
import com.example.cookbook.recipesPage.RecipeViewModel
import com.example.cookbook.utils.RecyclerItemClickListenr
import kotlinx.android.synthetic.main.fragment_social.*

/**
 * A simple [Fragment] subclass.
 */
class SocialFragment : Fragment() {

    lateinit var viewmodel:RecipeViewModel
    var sharedRecipeAsMap:MutableList<Map<String,Any>> = mutableListOf()
    private var mListAdapter: SharedRecipeAdapter = SharedRecipeAdapter(sharedRecipeAsMap)

    companion object {
        fun newInstance(): SocialFragment {
            return SocialFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_social, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.initViewModel()
        this.fetchSharedRecipeMap()
        this.initRecyclerView()
    }


    //------------------- INIT -------------------

    private fun initViewModel(){
        viewmodel = ViewModelProviders.of(activity!!).get(RecipeViewModel::class.java)
    }
    //configure recycler view : set layout manager and adapter
    private fun initRecyclerView() {
        shared_recipes_recyclerview.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mListAdapter
        }
        shared_recipes_recyclerview.addOnItemTouchListener(RecyclerItemClickListenr(requireContext(), shared_recipes_recyclerview, object : RecyclerItemClickListenr.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(context, RecipeDetailsActivity::class.java)
                val map =  HashMap<String,Any>(sharedRecipeAsMap[position])
                intent.putExtra("sharedRecipe",map)

                startActivity(intent)
            }
            override fun onItemLongClick(view: View?, position: Int) {
                print(position)
            }
        }))
    }

    //------------------- ASYNC -------------------

    fun fetchSharedRecipeMap(){
        viewmodel.sharedRecipesList.observe(viewLifecycleOwner, Observer {
            list -> updateItemsList(list) }
        )
    }

    // ------------------- UTILS -----------------

    private fun updateItemsList(recipesList:MutableList<Map<String,Any>>){
        sharedRecipeAsMap.clear()
        sharedRecipeAsMap.addAll(recipesList)
        mListAdapter.updateList(recipesList)
    }


}