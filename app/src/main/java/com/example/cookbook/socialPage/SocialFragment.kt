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
import com.example.cookbook.R
import com.example.cookbook.recipeDetailsPage.RecipeDetailsActivity
import com.example.cookbook.recipesPage.RecipeViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_social.*
import kotlinx.android.synthetic.main.recyclerview_social_item.*

/**
 * A simple [Fragment] subclass.
 */
class SocialFragment : Fragment() {

    lateinit var viewmodel:RecipeViewModel
    var sharedRecipeAsMap:MutableList<Map<String,Any>> = mutableListOf()
    private var mListAdapter: SharedRecipeAdapter = SharedRecipeAdapter(sharedRecipeAsMap)
    var firestoreDB = FirebaseFirestore.getInstance()

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
        viewmodel.observerOnSharedRecipe()
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

        mListAdapter.setOnItemClickListener(object: SharedRecipeAdapter.Listener{
            override fun onItemClick(position: Int) {
                val intent = Intent(context, RecipeDetailsActivity::class.java)
                val map =  HashMap<String,Any>(sharedRecipeAsMap[position])
                intent.putExtra("sharedRecipe",map)

                startActivity(intent)
            }

            override fun onClickLikeButton(recipe: Map<String, Any>) {
                val recipeDocRef =
                        firestoreDB.collection("sharedRecipes")
                                .document("${recipe["document_id"]}")
                val counterDocRef =
                        recipeDocRef
                                .collection("counter")
                                .document("${recipe["document_id"]}_counter")
                !like_button.isChecked
                viewmodel.likeRecipe(counterDocRef,recipeDocRef,5)
            }
        })
    }

    //------------------- ASYNC -------------------

    private fun fetchSharedRecipeMap(){
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