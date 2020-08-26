package com.example.smartice_closet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.smartice_closet.fragments.closetFragment
import com.example.smartice_closet.fragments.homeFragment
import com.example.smartice_closet.fragments.likeFragment
import com.example.smartice_closet.fragments.profileFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.android.synthetic.main.activity_main.*

class main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = homeFragment()
        val likeFragment = likeFragment()
        val closetFragment = closetFragment()
        val profileFragment = profileFragment()

        val chipNavigationBar : ChipNavigationBar = findViewById(R.id.bottom_navBar)
        chipNavigationBar.setItemSelected(R.id.home, true)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        chipNavigationBar.setOnItemSelectedListener { id ->
            when(id) {
                R.id.home -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

                R.id.closet -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, closetFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

                R.id.like -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, likeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

                R.id.profile -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, profileFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
            }
            true
        }
    }
}

