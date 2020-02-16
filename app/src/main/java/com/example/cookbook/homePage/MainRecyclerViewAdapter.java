package com.example.cookbook.homePage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;
import com.example.cookbook.models.Recipe;

import java.util.List;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewViewHolder> {

    private Context mContext;
    private RecyclerView.RecycledViewPool mViewPool;
    private HorizontalRecyclerViewAdapter mHorizontalRecyclerViewAdapter;
    private String[] mCategoryTitles;
    private List<List<Recipe>> mMainEmbeddedRecipeList;

    public MainRecyclerViewAdapter(List<List<Recipe>> mainEmbeddedRecipeList) {
        this.mMainEmbeddedRecipeList = mainEmbeddedRecipeList;
    }

    @NonNull
    @Override
    public MainRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recyclerview_main_item, parent, false);

        mViewPool = new RecyclerView.RecycledViewPool();
        mCategoryTitles = mContext.getResources().getStringArray(R.array.titles_main_recycler_view);

        return new MainRecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerViewViewHolder holder, int position) {
        holder.mHorizontalRecyclerView.setRecycledViewPool(mViewPool);
        holder.mHorizontalRecyclerView.setAdapter(new HorizontalRecyclerViewAdapter(mMainEmbeddedRecipeList.get(position)));
        holder.mTitlesCategory.setText(mCategoryTitles[position]);
        holder.mHorizontalRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void notifyItemChanged(List<List<Recipe>> embeddedList) {
        this.mMainEmbeddedRecipeList.clear();
        this.mMainEmbeddedRecipeList.addAll(embeddedList);
        notifyDataSetChanged();
    }
}
