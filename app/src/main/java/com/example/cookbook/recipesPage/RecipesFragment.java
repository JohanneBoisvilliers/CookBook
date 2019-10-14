package com.example.cookbook.recipesPage;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.cookbook.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesFragment extends Fragment {

    @BindView(R.id.all_recipes_recyclerview)
    RecyclerView mRecipesRecyclerView;

    private RecipesListAdapter mListAdapter;

    public RecipesFragment() {
        // Required empty public constructor
    }

    public static RecipesFragment newInstance() {
        return new RecipesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(this, view);

        this.configureRecyclerview();

        return view;
    }

    private void configureRecyclerview() {
        mListAdapter = new RecipesListAdapter();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecipesRecyclerView.setAdapter(mListAdapter);
        mRecipesRecyclerView.setLayoutManager(layoutManager);
    }

}
