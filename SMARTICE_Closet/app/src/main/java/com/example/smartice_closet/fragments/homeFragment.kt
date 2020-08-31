package com.example.smartice_closet.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartice_closet.R
import com.example.smartice_closet.camera
import com.example.smartice_closet.todayCody
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 * Use the [homeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class homeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todayCody_cV.setOnClickListener{
            val intent = Intent(context, todayCody::class.java)
            startActivity(intent)
        }

        camer_cV.setOnClickListener {
            val intent = Intent(context, camera::class.java)
            startActivity(intent)
        }




    }
}