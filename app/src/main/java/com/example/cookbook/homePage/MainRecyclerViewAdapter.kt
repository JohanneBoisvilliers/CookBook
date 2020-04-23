package com.example.cookbook.homePage

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.example.cookbook.R
import com.example.cookbook.models.Recipe
import com.example.cookbook.recipeDetailsPage.RecipeDetailsActivity
import com.example.cookbook.utils.RecyclerItemClickListenr
import kotlinx.android.synthetic.main.fragment_recipes.*
import kotlinx.android.synthetic.main.recyclerview_main_item.view.*

class MainRecyclerViewAdapter(private val mMainEmbeddedRecipeList: LinkedHashMap<String, List<Recipe>>) : RecyclerView.Adapter<MainRecyclerViewViewHolder>() {
    private var mContext: Context? = null
    private var mViewPool: RecycledViewPool? = null
    private val mHorizontalRecyclerViewAdapter: HorizontalRecyclerViewAdapter? = null
    private lateinit var mCategoryTitles: Array<String>
    private var mRecipeList: MutableList<Map<String, Any>>? = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewViewHolder {
        mContext = parent.context
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.recyclerview_main_item, parent, false)
        mViewPool = RecycledViewPool()
        mCategoryTitles = mContext!!.resources.getStringArray(R.array.titles_main_recycler_view)
        return MainRecyclerViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainRecyclerViewViewHolder, position: Int) {
        val actualRecipeList = mMainEmbeddedRecipeList.values.elementAt(position)
        holder.itemView.category_title.text = mMainEmbeddedRecipeList.keys.elementAt(position)
        this.initRecyclerViewAtPosition(holder, actualRecipeList)
    }

    override fun getItemCount(): Int {
        return mMainEmbeddedRecipeList.size
    }

    fun notifyItemChanged(embeddedList: LinkedHashMap<String, List<Recipe>>?, recipeAsMap: MutableList<Map<String, Any>>?, position: Int) {
        recipeAsMap?.let {
            mRecipeList?.clear()
            mRecipeList?.addAll(recipeAsMap)
        }
        mMainEmbeddedRecipeList.clear()
        mMainEmbeddedRecipeList.putAll(embeddedList!!)
        notifyItemChanged(position)
    }

    //------------------- INIT ---------------

    private fun initRecyclerViewAtPosition(holder: MainRecyclerViewViewHolder, list: List<Recipe>) {
        if (mMainEmbeddedRecipeList.isNotEmpty()) {
            holder.itemView.horizontal_recycler_view.setRecycledViewPool(mViewPool)
            holder.itemView.horizontal_recycler_view.apply {
                adapter = HorizontalRecyclerViewAdapter(list)
                layoutManager = LinearLayoutManager(mContext,
                        LinearLayoutManager.HORIZONTAL, false)
            }
            holder.itemView.horizontal_recycler_view.addOnItemTouchListener(RecyclerItemClickListenr(mContext!!,
                    holder.itemView.horizontal_recycler_view,
                    object : RecyclerItemClickListenr.OnItemClickListener {

                        override fun onItemClick(view: View, position: Int) {
                            val isRecipeObject = list[position].baseDataRecipe?.baseRecipeId == 0L
                            val intent = Intent(mContext, RecipeDetailsActivity::class.java)
                            if (!isRecipeObject) {
                                intent.putExtra("recipe", list[position].baseDataRecipe?.baseRecipeId)
                            } else {
                                val map = HashMap<String, Any>(mRecipeList?.get(position)!!)
                                intent.putExtra("sharedRecipe", map)
                            }

                            startActivity(mContext!!, intent, null)
                        }

                        override fun onItemLongClick(view: View?, position: Int) {
                            print(position)
                        }
                    }))
        }
    }
}

