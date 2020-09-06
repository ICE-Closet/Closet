package com.example.smartice_closet.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartice_closet.R

class BottomAdapter (val bottomWear : ArrayList<String>) : RecyclerView.Adapter<BottomAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bottomName : TextView = itemView.findViewById(R.id.bottomName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_bottom, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bottomWear.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bottomName.text = bottomWear[position]
    }
}