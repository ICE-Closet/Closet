package com.example.smartice_closet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartice_closet.models.Model
import com.example.smartice_closet.R
import com.example.smartice_closet.fragments.closetFragment
import kotlinx.android.synthetic.main.row.view.*

class recyclerAdapter(val arrayList: ArrayList<Model>, val context: closetFragment) : RecyclerView.Adapter<recyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(model: Model) {
            itemView.titleTv.text = model.title
            itemView.descriptionTv.text = model.des
            itemView.image.setImageResource(model.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

        holder.itemView.setOnClickListener {
//            if (position == 0) {
//                Log.d("onBindViewHolder", "TOP Checked!")
//                // Top 사진이 있는 Activity로 전환
//
//
//            }
//            else if (position == 1) {
//                Log.d("onBindViewHolder", "Bottom Checked!")
//                // Bottom 사진이 있는 Activity로 전환
//            }
//            else if (position == 2) {
//                Log.d("onBindViewHolder", "Outer Checked!")
//                // Outer 사진이 있는 Activity로 전환
//            }
//            else if (position == 3) {
//                Log.d("onBindViewHolder", "Dress5 Checked!")
//                // Dress 사진이 있는 Activity로 전환
//            }


        }
    }
}