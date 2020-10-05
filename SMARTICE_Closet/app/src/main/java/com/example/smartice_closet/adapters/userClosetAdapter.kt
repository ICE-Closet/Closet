package com.example.smartice_closet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartice_closet.R
import com.example.smartice_closet.models.userClosetModel
import kotlinx.android.synthetic.main.row_user_closet.view.*

class userClosetAdapter(val userCodyList: ArrayList<userClosetModel>) : RecyclerView.Adapter<userClosetAdapter.viewHolder>() {

    inner class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun setImages(data : userClosetModel) {
            Glide.with(itemView.context)
                .load(data.image)
                .into(itemView.userCloset_iV)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userClosetAdapter.viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_user_closet, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return userCodyList.size
    }

    override fun onBindViewHolder(holder: userClosetAdapter.viewHolder, position: Int) {
        holder.setImages(userCodyList[position])
    }
}