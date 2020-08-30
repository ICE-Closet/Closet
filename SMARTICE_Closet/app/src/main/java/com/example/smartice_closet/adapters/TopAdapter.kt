package com.example.smartice_closet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartice_closet.R

class TopAdapter(val topWear : ArrayList<String>) : RecyclerView.Adapter<TopAdapter.ViewHolder>() {     // String을 Image로 바꿔야함

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topName: TextView = itemView.findViewById(R.id.topName)     // TextView를 ImageView로 바꿔야 함

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_top, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return topWear.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.topName.text = topWear[position]
    }
}