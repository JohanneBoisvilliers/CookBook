package com.example.cookbook.homePage;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainRecyclerViewViewHolder extends RecyclerView.ViewHolder {

    public MainRecyclerViewViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
