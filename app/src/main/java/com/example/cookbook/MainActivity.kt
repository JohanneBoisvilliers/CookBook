package com.example.cookbook

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.cookbook.database.CookBookLocalDatabase
import com.example.cookbook.injections.Injections
import com.example.cookbook.recipesPage.RecipeViewModel
import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mRecipeViewModel: RecipeViewModel? = null
    private val PERMISSIONS:Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Stetho.initializeWithDefaults(this)
        configureViewPager()
        configureBottomView()
        configureRecipeViewModel()
        val cookBookLocalDatabase = CookBookLocalDatabase.getInstance(this)
        // TODO enlever cette ligne qui ne sert qu'à instancier la BBD
        cookBookLocalDatabase.recipeDao().getRecipes().observe(this, Observer { list -> Log.d("DEbug", "onCreate: " + list[0].baseDataRecipe?.name) })
        // TODO créer vraie méthode pour gestion de permission
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSIONS)

            }

    }

    // ----------------------------------- UTILS -----------------------------------
    private fun configureViewPager() {
        activity_main_viewpager.adapter = MainViewPagerAdapter(supportFragmentManager)
        activity_main_viewpager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                bottom_navigation.menu.getItem(position).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun configureBottomView() {
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            updateMainFragment(item.itemId)
            false
        }
    }

    private fun updateMainFragment(integer: Int) {
        when (integer) {
            R.id.nav_home -> activity_main_viewpager.currentItem = 0
            R.id.nav_recipes -> activity_main_viewpager.currentItem = 1
            R.id.nav_social -> activity_main_viewpager.currentItem = 2
            R.id.nav_user -> activity_main_viewpager.currentItem = 3
            R.id.nav_add -> activity_main_viewpager.currentItem = 4
        }
    }

    private fun configureRecipeViewModel() {
        val viewModelFactory = Injections.provideViewModelFactory(this)
        mRecipeViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModel::class.java)
    }
}