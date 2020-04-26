package com.example.cookbook

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.cookbook.homePage.HomeFragment
import com.example.cookbook.profilePage.ProfileFragment
import com.example.cookbook.recipesPage.RecipesFragment
import com.example.cookbook.socialPage.SocialFragment

class MainViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> RecipesFragment.newInstance()
            2 -> SocialFragment.newInstance()
            3 -> ProfileFragment.newInstance()
            else -> HomeFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 4
    }
}