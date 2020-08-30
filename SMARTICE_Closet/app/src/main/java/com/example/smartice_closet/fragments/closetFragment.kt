package com.example.smartice_closet.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartice_closet.Model
import com.example.smartice_closet.R
import com.example.smartice_closet.adapters.recyclerAdapter
import kotlinx.android.synthetic.main.fragment_closet.*

/**
 * A simple [Fragment] subclass.
 * Use the [closetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class closetFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_closet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayList = ArrayList<Model>()
        arrayList.add(Model("Top", "Check your Top clothing", R.drawable.topwear))
        arrayList.add(Model("Bottom", "Check your Bottom clothing", R.drawable.bottomwear))
        arrayList.add(Model("Outer", "Check your Outer clothing", R.drawable.outerwear))
        arrayList.add(Model("Dress", "Check your Dress clothing", R.drawable.dresswear))

        val Adapter =
            recyclerAdapter(arrayList, this)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = Adapter

    }

}