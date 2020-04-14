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

import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.example.cookbook.onlineREcipe.RecipeOnlineActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private var mMainAdapter: MainRecyclerViewAdapter? = null
    private var mRecipeViewModel: RecipeViewModel? = null

    private val mFinalEmbeddedList: MutableList<List<Recipe>>? = null
    private val mLatestRecipes: List<Recipe>? = null
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
    }

    // ----------------------------------- INIT -----------------------------------
    private fun initMainRecyclerView() {
        main_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mMainAdapter
        }
    }

    private fun initViewModel() {
        mRecipeViewModel = ViewModelProviders.of(activity!!).get(RecipeViewModel::class.java)
    }

    // ----------------------------------- UI -----------------------------------


    private fun updateItemList(embeddedList: List<List<Recipe>>) {
        this.mFinalEmbeddedList?.clear()
        this.mFinalEmbeddedList?.addAll(embeddedList)
        this.mMainAdapter!!.notifyItemChanged(mFinalEmbeddedList)
    }

    // ----------------------------------- OBSERVERS -----------------------------------

    private fun observerOnArticle(){
        mRecipeViewModel?.article?.observe(viewLifecycleOwner, Observer {
            headline_title.text = it.description
            val circularProgressDrawable = CircularProgressDrawable(context!!)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide.with(context!!)
                    .load(it.photoUrl)
                    .placeholder(circularProgressDrawable)
                    .centerCrop()
                    .into(headline_photo_container)
            this.listenerOnHeadLineCard(it.url)
        })
    }

    // ----------------------------------- LISTENERS -----------------------------------

    private fun listenerOnHeadLineCard(url:String){
        article_card.setOnClickListener {
            val intent = Intent(context!!, RecipeOnlineActivity::class.java)
            intent.putExtra("url", url)
            startActivity(intent)
        }
    }

}
