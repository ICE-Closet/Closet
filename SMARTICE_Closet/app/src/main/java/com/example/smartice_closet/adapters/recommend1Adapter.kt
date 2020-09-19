package com.example.smartice_closet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartice_closet.R
import com.example.smartice_closet.models.recommend1
import kotlinx.android.synthetic.main.row_first_recommend.view.*

class recommend1Adapter(val cody1List: ArrayList<recommend1>): RecyclerView.Adapter<recommend1Adapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(data : recommend1) {
            Glide.with(itemView.context)
                .load(data.image)
                .into(itemView.recommend1_cody_iV)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_first_recommend, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cody1List.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(cody1List[position])
    }
}