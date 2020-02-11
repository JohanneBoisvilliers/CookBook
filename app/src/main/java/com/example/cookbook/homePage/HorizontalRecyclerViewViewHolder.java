package com.example.cookbook.homePage;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;
import com.example.cookbook.models.Recipe;

import butterknife.BindView;

public class HorizontalRecyclerViewViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.recipe_name_card_view)
    TextView recipeName;

    public HorizontalRecyclerViewViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void updateUiOfRecipe(Recipe recipe){
//        recipeName.setText(recipe.name);
    }
}
