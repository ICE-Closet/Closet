package com.example.smartice_closet.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartice_closet.R
import com.example.smartice_closet.models.bottomFrequencies
import kotlinx.android.synthetic.main.row_bottom.view.*

class BottomAdapter (val bottomWear : ArrayList<bottomFrequencies>) : RecyclerView.Adapter<BottomAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(data : bottomFrequencies) {
            Glide.with(itemView.context)
                .load(data.image)
                .into(itemView.bottomFrequency_iV)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_bottom, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bottomWear.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(bottomWear[position])
    }
}