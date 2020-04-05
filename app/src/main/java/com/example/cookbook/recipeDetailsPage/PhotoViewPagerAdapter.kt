package com.example.cookbook.recipeDetailsPage

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.cookbook.models.Photo


class PhotoViewPagerAdapter constructor(photoList:MutableList<Photo>) : RecyclerView.Adapter<PhotoViewPagerViewHolder>() {
    var list = mutableListOf<String>()
    private var mPhotoList: MutableList<Photo> = photoList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewPagerViewHolder {
        return PhotoViewPagerViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return mPhotoList.size
    }

    override fun onBindViewHolder(holder: PhotoViewPagerViewHolder, position: Int) {
        holder.bind(mPhotoList[position])
    }

    fun getItemPosition(test: Any?): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun updatePhotoList(list:MutableList<Photo>){
        mPhotoList.clear()
        mPhotoList.addAll(list)
        notifyDataSetChanged()
    }

}