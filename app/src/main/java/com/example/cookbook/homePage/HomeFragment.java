package com.example.cookbook.homePage;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.main_recycler_view)
    RecyclerView mMainRecyclerView;

    private MainRecyclerViewAdapter mMainAdapter;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        this.configureMainRecyclerView();

        return view;
    }

    // ----------------------------------- UTILS -----------------------------------
    private void configureMainRecyclerView() {
        this.mMainAdapter = new MainRecyclerViewAdapter();
        this.mMainRecyclerView.setAdapter(this.mMainAdapter);
        this.mMainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
