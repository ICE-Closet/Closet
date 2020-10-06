package com.example.smartice_closet.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartice_closet.R
import com.example.smartice_closet.models.outerFrequencies
import kotlinx.android.synthetic.main.row_outer.view.*
import kotlinx.android.synthetic.main.row_top.view.*

class OuterAdapter(val outerWear : ArrayList<outerFrequencies>) : RecyclerView.Adapter<OuterAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(data : outerFrequencies) {
            Glide.with(itemView.context)
                .load(data.image)
                .into(itemView.outerFrequency_iV)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_outer, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return outerWear.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(outerWear[position])
    }

}