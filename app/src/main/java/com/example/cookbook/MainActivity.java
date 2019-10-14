package com.example.cookbook;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.configureViewPager();
        this.configureBottomView();
    }

    // ----------------------------------- UTILS -----------------------------------
    private void configureViewPager() {
        ViewPager pager = mViewPager;
        pager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void configureBottomView() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                updateMainFragment(item.getItemId());
                return false;
            }
        });
    }

    private void updateMainFragment(Integer integer) {
        switch (integer) {
            case R.id.nav_home:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.nav_recipes:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.nav_social:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.nav_user:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.nav_add:
                mViewPager.setCurrentItem(4);
                break;
        }
    }
}
