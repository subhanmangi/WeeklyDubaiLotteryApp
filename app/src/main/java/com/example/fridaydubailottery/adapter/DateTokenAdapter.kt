package com.example.fridaydubailottery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fridaydubailottery.R
import com.robinhood.ticker.TickerView


class DateTokenAdapter(var items: ArrayList<String>, var context: Context, var isForToken:Boolean) :
    RecyclerView.Adapter<DateTokenAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.dates_or_tokens_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(isForToken){
            holder.tvDateToke?.visibility=View.INVISIBLE
            holder.tvFirstPrize?.visibility=View.VISIBLE
            holder.tvFirstPrize?.setPreferredScrollingDirection(TickerView.ScrollingDirection.DOWN)
            holder.tvFirstPrize?.text= items[position]

        }else{
            holder.tvDateToke?.visibility=View.VISIBLE
            holder.tvFirstPrize?.visibility=View.GONE
            holder.tvDateToke?.text= items[position]
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        var tvDateToke: TextView? = null
        var tvFirstPrize: TickerView? = null

        init {
            tvDateToke = view.findViewById(R.id.tvDateToke)
            tvFirstPrize = view.findViewById(R.id.tvFirstPrize)
        }
    }
}