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
    private val GENDER = "USERGENDER"

    var userToken = ""
    var userGender = ""

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
        userGender = bundle!!.getString(GENDER).toString()

        Log.d("onViewCreated", userName + userToken + userGender)

        userCloset.text = "${userName}'s Closet"


        val arrayList = ArrayList<Model>()
        arrayList.add(
            Model("top", "Check your Top clothes", R.drawable.topwear)
        )
        arrayList.add(
            Model("bottom", "Check your Bottom clothes", R.drawable.bottomwear)
        )
        arrayList.add(
            Model("outer", "Check your Outer clothes", R.drawable.outerwear)
        )
        arrayList.add(
            Model("dress", "Check your Dress clothes", R.drawable.dresswear)
        )

        val Adapter = recyclerAdapter(arrayList, this, userToken, userGender)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = Adapter


    }
}