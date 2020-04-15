package com.example.cookbook.homePage


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

import com.example.cookbook.R
import com.example.cookbook.models.Recipe
import com.example.cookbook.recipesPage.RecipeViewModel

import com.bumptech.glide.Glide
import com.example.cookbook.onlineREcipe.RecipeOnlineActivity
import com.facebook.internal.Mutable
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private var mMainAdapter = MainRecyclerViewAdapter(linkedMapOf())
    private var viewModel: RecipeViewModel? = null

    private val mFinalEmbeddedList: LinkedHashMap<String,List<Recipe>> = linkedMapOf()
    private val mRandomRecipes: MutableList<Recipe> = mutableListOf()
    private val mFavoriteRecipes: List<Recipe>? = null
    private val mNotDoneRecipes: List<Recipe>? = null

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        this.initViewModel()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.initMainRecyclerView()
        this.observerOnArticle()
        this.observerOnRandomRecipes()
    }

    // ----------------------------------- INIT -----------------------------------
    private fun initMainRecyclerView() {
        main_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mMainAdapter
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(activity!!).get(RecipeViewModel::class.java)
    }

    // ----------------------------------- UI -----------------------------------


    private fun updateItemList(embeddedList: LinkedHashMap<String,List<Recipe>>) {
        this.mMainAdapter.notifyItemChanged(embeddedList)
    }

    // ----------------------------------- OBSERVERS -----------------------------------

    private fun observerOnArticle(){
        // when article is fetch from firebase
        viewModel?.article?.observe(viewLifecycleOwner, Observer {
            // set article name
            headline_title.text = it.description
            // create a circular progress bar for article photo loading
            val circularProgressDrawable = CircularProgressDrawable(context!!)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide.with(context!!)
                    .load(it.photoUrl)
                    .placeholder(circularProgressDrawable)
                    .centerCrop()
                    .into(headline_photo_container)
            // put a listener on the article card : when user click on card, open the
            // article online in a webview
            this.listenerOnHeadLineCard(it.url)
        })
    }

    private fun observerOnRandomRecipes(){
        viewModel?.randomRecipes?.observe(viewLifecycleOwner, Observer {
            this.mRandomRecipes.clear()
            this.mRandomRecipes.addAll(it)
            mFinalEmbeddedList["Random Recipes"] = mRandomRecipes
            this.updateItemList(mFinalEmbeddedList)
        })
    }

    // ----------------------------------- LISTENERS -----------------------------------

    private fun listenerOnHeadLineCard(url:String){
        article_card.setOnClickListener {
            val intent = Intent(context!!, RecipeOnlineActivity::class.java)
            // set intent extra with the url from HeadLineArticle object from firebase
            intent.putExtra("url", url)
            startActivity(intent)
        }
    }

}
