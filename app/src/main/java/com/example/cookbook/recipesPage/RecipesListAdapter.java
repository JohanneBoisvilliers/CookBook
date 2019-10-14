package com.example.cookbook.recipesPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;

import java.util.Random;

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListViewHolder> {

    private Context mContext;
    private Random mRandom;

    @NonNull
    @Override
    public RecipesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.recyclerview_all_recipes_item, parent, false);
        return new RecipesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesListViewHolder holder, int position) {
        mRandom = new Random();
        holder.mPhotoContainer.getLayoutParams().height = getRandomHeight(550, 800);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public int getRandomHeight(int min, int max) {
        return mRandom.nextInt((max - min) + 1) + min;
    }
}
