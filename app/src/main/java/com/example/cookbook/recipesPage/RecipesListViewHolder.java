package com.example.cookbook.recipesPage;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.photo_container)
    ImageView mPhotoContainer;

    public RecipesListViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
