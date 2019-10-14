package com.example.cookbook;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.cookbook.addRecipePage.AddRecipeFragment;
import com.example.cookbook.homePage.HomeFragment;
import com.example.cookbook.profilePage.ProfileFragment;
import com.example.cookbook.recipesPage.RecipesFragment;
import com.example.cookbook.socialPage.SocialFragment;

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {

    public MainViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return RecipesFragment.newInstance();
            case 2:
                return SocialFragment.newInstance();
            case 3:
                return ProfileFragment.newInstance();
            case 4:
                return AddRecipeFragment.newInstance();
            default:
                return HomeFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}