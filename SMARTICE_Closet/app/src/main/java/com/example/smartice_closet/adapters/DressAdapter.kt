package com.example.smartice_closet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartice_closet.R

class DressAdapter(val dressWear : ArrayList<String>) : RecyclerView.Adapter<DressAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dressName: TextView = itemView.findViewById(R.id.dressName)     // TextView를 ImageView로 바꿔야 함

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_dress, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dressWear.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dressName.text = dressWear[position]
    }
}