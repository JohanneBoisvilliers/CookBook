package com.example.cookbook.homePage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewViewHolder> {

    private Context mContext;
    private RecyclerView.RecycledViewPool mViewPool;
    private HorizontalRecyclerViewAdapter mHorizontalRecyclerViewAdapter;

    @NonNull
    @Override
    public MainRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recyclerview_main_item, parent, false);

        mViewPool = new RecyclerView.RecycledViewPool();
        mHorizontalRecyclerViewAdapter = new HorizontalRecyclerViewAdapter();

        return new MainRecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerViewViewHolder holder, int position) {
        holder.mHorizontalRecyclerView.setRecycledViewPool(mViewPool);
        holder.mHorizontalRecyclerView.setAdapter(mHorizontalRecyclerViewAdapter);
        holder.mHorizontalRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
