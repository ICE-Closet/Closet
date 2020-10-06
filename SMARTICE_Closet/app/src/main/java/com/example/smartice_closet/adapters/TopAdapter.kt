package com.example.smartice_closet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartice_closet.R
import com.example.smartice_closet.models.topFrequencies
import kotlinx.android.synthetic.main.row_top.view.*

class TopAdapter(val topWear : ArrayList<topFrequencies>) : RecyclerView.Adapter<TopAdapter.ViewHolder>() {     // String을 Image로 바꿔야함

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(data : topFrequencies) {
            Glide.with(itemView.context)
                .load(data.image)
                .into(itemView.topFrequency_iV)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_top, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return topWear.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(topWear[position])
    }
}