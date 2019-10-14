package com.example.cookbook.homePage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;

public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerViewViewHolder> {


    @NonNull
    @Override
    public HorizontalRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_horizontal_home_item, parent, false);
        return new HorizontalRecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalRecyclerViewViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
