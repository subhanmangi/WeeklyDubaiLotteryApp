package com.example.fridaydubailottery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fridaydubailottery.R


class ImageAdapter(var items: List<String>, var context: Context) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.image_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(items[position]).into(holder.roundedImageView!!);
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        var roundedImageView: ImageView? = null

        init {
            roundedImageView = view.findViewById(R.id.roundedImageView)
        }
    }
}