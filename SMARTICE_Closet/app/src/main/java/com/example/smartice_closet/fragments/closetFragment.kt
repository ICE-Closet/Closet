package com.example.smartice_closet.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartice_closet.models.Model
import com.example.smartice_closet.R
import com.example.smartice_closet.adapters.recyclerAdapter
import com.example.smartice_closet.userCloset.viewUserCloset
import kotlinx.android.synthetic.main.fragment_closet.*

/**
 * A simple [Fragment] subclass.
 * Use the [closetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class closetFragment : Fragment() {

    private val USERNAME = "USERNAME"
    private val TOKEN = "USERTOKEN"
    var userToken = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_closet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val userName = bundle!!.getString(USERNAME)
        userToken = bundle!!.getString(TOKEN).toString()
        Log.d("onViewCreated", userName + userToken)

        userCloset.text = "${userName}'s Closet"

        val arrayList = ArrayList<Model>()
        arrayList.add(
            Model("top", "Check your Top clothing", R.drawable.topwear)
        )
        arrayList.add(
            Model("bottom", "Check your Bottom clothing", R.drawable.bottomwear)
        )
        arrayList.add(
            Model("outer", "Check your Outer clothing", R.drawable.outerwear)
        )
        arrayList.add(
            Model("dress", "Check your Dress clothing", R.drawable.dresswear)
        )

        val Adapter = recyclerAdapter(arrayList, this, userToken)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = Adapter


    }
}