package com.example.smartice_closet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartice_closet.R
import com.example.smartice_closet.models.recommend3
import kotlinx.android.synthetic.main.row_third_recommend.view.*

class recommend3Adapter(val cody3List : ArrayList<recommend3>) : RecyclerView.Adapter<recommend3Adapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(data : recommend3) {
            Glide.with(itemView.context)
                .load(data.image)
                .into(itemView.recommend3_cody_iV)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_third_recommend, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cody3List.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(cody3List[position])
    }
}