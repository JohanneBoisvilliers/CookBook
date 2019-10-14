package com.example.cookbook.addRecipePage;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.cookbook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddRecipeFragment extends Fragment {


    public AddRecipeFragment() {
        // Required empty public constructor
    }

    public static AddRecipeFragment newInstance() {
        return new AddRecipeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_recipe, container, false);
    }

}
