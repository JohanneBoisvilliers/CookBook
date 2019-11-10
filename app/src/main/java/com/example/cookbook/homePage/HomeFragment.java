package com.example.cookbook.homePage;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;
import com.example.cookbook.models.Recipe;
import com.example.cookbook.recipesPage.RecipeViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.main_recycler_view)
    RecyclerView mMainRecyclerView;

    private MainRecyclerViewAdapter mMainAdapter;
    private RecipeViewModel mRecipeViewModel;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private List<List<Recipe>> mFinalEmbeddedList;
    private List<Recipe> mLatestRecipes;
    private List<Recipe> mFavoriteRecipes;
    private List<Recipe> mNotDoneRecipes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        this.configureMainRecyclerView();
        this.configureViewRecipeViewModel();
        return view;
    }

    // ----------------------------------- UTILS -----------------------------------
    private void configureMainRecyclerView() {
        this.mMainAdapter = new MainRecyclerViewAdapter(mFinalEmbeddedList);
        this.mMainRecyclerView.setAdapter(this.mMainAdapter);
        this.mMainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void configureViewRecipeViewModel() {
        mRecipeViewModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
    }

    private void updateItemList(List<List<Recipe>> embeddedList) {
        this.mFinalEmbeddedList.clear();
        this.mFinalEmbeddedList.addAll(embeddedList);
        this.mMainAdapter.notifyItemChanged(mFinalEmbeddedList);
    }

    // ----------------------------------- ASYNC -----------------------------------
    private void getFavoriteRecipes() {
        mRecipeViewModel.getRecipes().observe(this, list -> {

        });
    }

}
