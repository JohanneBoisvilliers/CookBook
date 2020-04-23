package com.example.cookbook

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.cookbook.database.CookBookLocalDatabase
import com.example.cookbook.injections.Injections
import com.example.cookbook.profilePage.ProfileViewModel
import com.example.cookbook.recipesPage.RecipeViewModel
//import com.facebook.flipper.android.AndroidFlipperClient
//import com.facebook.flipper.android.utils.FlipperUtils
//import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
//import com.facebook.flipper.plugins.inspector.DescriptorMapping
//import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
//import com.facebook.soloader.SoLoader
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mRecipeViewModel: RecipeViewModel? = null

    var PERMISSION_ALL = 1
    var PERMISSIONS = arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS,
            Manifest.permission.CAMERA
    )

    // ----------------------------------- LIFE CYCLE -----------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //SoLoader.init(this, false)

        //if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(application)) {
        //    val client = AndroidFlipperClient.getInstance(application)
        //    client.addPlugin(InspectorFlipperPlugin(application, DescriptorMapping.withDefaults()))
        //    client.addPlugin( DatabasesFlipperPlugin(application));
        //    client.start()
        //}
        configureViewPager()
        configureBottomView()
        configureRecipeViewModel()
        val cookBookLocalDatabase = CookBookLocalDatabase.getInstance(this)

        // TODO créer vraie méthode pour gestion de permission
        if (!hasPermissions(this, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        mRecipeViewModel?.getSharedRecipes()
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
        }
    }

    private fun configureRecipeViewModel() {
        val viewModelFactory = Injections.provideViewModelFactory(this)
        mRecipeViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModel::class.java)

    }

    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }


}