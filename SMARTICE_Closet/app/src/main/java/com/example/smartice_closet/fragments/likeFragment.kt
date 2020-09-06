package com.example.smartice_closet.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.example.smartice_closet.R
import com.example.smartice_closet.adapters.BottomAdapter
import com.example.smartice_closet.adapters.DressAdapter
import com.example.smartice_closet.adapters.OuterAdapter
import com.example.smartice_closet.adapters.TopAdapter
import kotlinx.android.synthetic.main.fragment_like.*


/**
 * A simple [Fragment] subclass.
 * Use the [likeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class likeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_like, container, false)
    }


    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topWear: ArrayList<String> = ArrayList()  // 나중에 String을 이미지로 바꿔야 함
        val bottomWear: ArrayList<String> = ArrayList()
        val outerWear: ArrayList<String> = ArrayList()
        val dressWear: ArrayList<String> = ArrayList()

        for (i in 1..3) {
            topWear.add("Topwear #$i")
        }

        for (i in 1..3) {
            bottomWear.add("Bottomwear #$i")
        }

        for (i in 1..3) {
            outerWear.add("Outerwear #$i")
        }

        for (i in 1..3) {
            dressWear.add("Dresswear #$i")
        }

        topRecyclerView.layoutManager = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
        topRecyclerView.adapter = TopAdapter(topWear)

        bottomRecyclerView.layoutManager = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
        bottomRecyclerView.adapter = BottomAdapter(bottomWear)

        outerRecyclerView.layoutManager = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
        outerRecyclerView.adapter = OuterAdapter(outerWear)

        dressRecyclerView.layoutManager = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
        dressRecyclerView.adapter = DressAdapter(dressWear)


    }
}