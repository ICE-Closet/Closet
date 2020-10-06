package com.example.smartice_closet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.smartice_closet.fragments.closetFragment
import com.example.smartice_closet.fragments.homeFragment
import com.example.smartice_closet.fragments.likeFragment
import com.example.smartice_closet.fragments.profileFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.android.synthetic.main.activity_main.*

class main : AppCompatActivity() {

    private val USERNAME = "USERNAME"
    private val TOKEN = "USERTOKEN"
    private val USERGENDER = "USERGENDER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = homeFragment()
        val likeFragment = likeFragment()
        val closetFragment = closetFragment()
        val profileFragment = profileFragment()

        var userToken = intent.getStringExtra(TOKEN).toString()
        var userName = intent.getStringExtra(USERNAME).toString()
        var userGender = intent.getStringExtra(USERGENDER).toString()

        Log.d(TOKEN, userToken)
        Log.d(USERNAME, userName)
        Log.d(USERGENDER, userGender)

        val chipNavigationBar : ChipNavigationBar = findViewById(R.id.bottom_navBar)
        chipNavigationBar.setItemSelected(R.id.home, true)
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                homeFragment.apply {
                    arguments = Bundle().apply {
                        putString(USERNAME, userName)
                        putString(TOKEN, userToken)
                        putString(USERGENDER, userGender)
                    }
                })
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        chipNavigationBar.setOnItemSelectedListener { id ->
            when(id) {
                R.id.home -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            homeFragment.apply {
                                arguments = Bundle().apply {
                                    putString(USERNAME, userName)
                                    putString(TOKEN, userToken)
                                    putString(USERGENDER, userGender)
                                }
                            })
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commitNow()
                }

                R.id.closet -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            closetFragment.apply {
                                arguments = Bundle().apply {
                                    putString(USERNAME, userName)
                                    putString(TOKEN, userToken)
                                    putString(USERGENDER, userGender)
                                }
                            })
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commitNow()
                }

                R.id.like -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            likeFragment.apply {
                                arguments = Bundle().apply {
//                                    putString(USERNAME, userName)
                                    putString(TOKEN, userToken)
                                    putString(USERGENDER, userGender)
                                }
                            })
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commitNow()
                }

                R.id.profile -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            profileFragment.apply {
                                arguments = Bundle().apply {
                                    putString(USERNAME, userName)
                                    putString(TOKEN, userToken)
                                    putString(USERGENDER, userGender)
                                }
                            })
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commitNow()
                }
            }
            true
        }
    }

}

