package com.example.smartice_closet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartice_closet.R
import com.example.smartice_closet.models.recommend2
import kotlinx.android.synthetic.main.row_second_recommend.view.*

class recommend2Adapter(val cody2List : ArrayList<recommend2>) : RecyclerView.Adapter<recommend2Adapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(data : recommend2) {
            Glide.with(itemView.context)
                .load(data.image)
                .into(itemView.recommend2_cody_iV)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_second_recommend, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cody2List.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(cody2List[position])
    }
}