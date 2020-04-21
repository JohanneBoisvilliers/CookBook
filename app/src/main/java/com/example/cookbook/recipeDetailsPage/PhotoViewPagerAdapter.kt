package com.example.cookbook.recipeDetailsPage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.cookbook.R
import com.example.cookbook.models.Photo


class PhotoViewPagerAdapter <T>constructor(photoList:MutableList<T>) : RecyclerView.Adapter<PhotoViewPagerViewHolder<T>>() {
    private var mPhotoList: MutableList<T> = photoList
    private var mContext: Context? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewPagerViewHolder<T> {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(R.layout.viewpager_recipe_photo_item, parent, false)
        return PhotoViewPagerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mPhotoList.size
    }

    override fun onBindViewHolder(holder: PhotoViewPagerViewHolder<T>, position: Int) {
        holder.bind(mPhotoList[position])
    }

    fun updatePhotoList(list:MutableList<T>){
        mPhotoList.clear()
        mPhotoList.addAll(list)
        notifyDataSetChanged()
    }

}