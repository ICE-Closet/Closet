package com.example.smartice_closet.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartice_closet.R

class OuterAdapter(val outerWear : ArrayList<String>) : RecyclerView.Adapter<OuterAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val outerName : TextView = itemView.findViewById(R.id.outerName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_outer, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return outerWear.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.outerName.text = outerWear[position]


    }

}