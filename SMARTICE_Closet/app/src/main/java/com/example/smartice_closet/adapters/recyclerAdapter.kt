package com.example.smartice_closet.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.smartice_closet.models.Model
import com.example.smartice_closet.R
import com.example.smartice_closet.fragments.closetFragment
import com.example.smartice_closet.userCloset.viewUserCloset
import kotlinx.android.synthetic.main.row.view.*

class recyclerAdapter(val arrayList: ArrayList<Model>, val context: closetFragment, val userToken:String, val userGender: String) : RecyclerView.Adapter<recyclerAdapter.ViewHolder>() {

    private var CATEGORIES = "CATEGORIES"
    private var TOKEN = "USERTOKEN"

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, viewUserCloset::class.java).apply {
                    putExtra(TOKEN, userToken)
                    putExtra(CATEGORIES, "${itemView.titleTv.text}")
                }
                itemView.context.startActivity(intent)
            }
        }

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
    }
}