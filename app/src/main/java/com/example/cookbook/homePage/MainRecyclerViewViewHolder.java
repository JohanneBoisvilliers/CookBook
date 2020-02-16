package com.example.cookbook.homePage;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainRecyclerViewViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.horizontal_recycler_view)
    RecyclerView mHorizontalRecyclerView;
    @BindView(R.id.category_title)
    TextView mTitlesCategory;

    public MainRecyclerViewViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
