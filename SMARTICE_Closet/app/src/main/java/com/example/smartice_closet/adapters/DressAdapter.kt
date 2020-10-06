package com.example.smartice_closet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartice_closet.R
import com.example.smartice_closet.models.dressFrequencies
import com.example.smartice_closet.models.topFrequencies
import kotlinx.android.synthetic.main.row_dress.view.*
import kotlinx.android.synthetic.main.row_top.view.*

class DressAdapter(val dressWear : ArrayList<dressFrequencies>) : RecyclerView.Adapter<DressAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(data : dressFrequencies) {
            Glide.with(itemView.context)
                .load(data.image)
                .into(itemView.dressFrequency_iV)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_dress, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dressWear.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(dressWear[position])
    }
}