package com.example.smartice_closet.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.smartice_closet.R
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 * Use the [profileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class profileFragment : Fragment() {

    private val USERNAME = "USERNAME"
    private val TOKEN = "USERTOKEN"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val userName = bundle!!.getString(USERNAME)
        val userToken = bundle!!.getString(TOKEN)
        Log.d("onViewCreated", userName + userToken)

        setUserName(userName)

        update_btn.setOnClickListener {
            // Server Retrofit 구현 필요
            Toast.makeText(context, "정보가 업데이트 되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUserName(userName: String?) {
        user_Name.text = "${userName}".toUpperCase()
        small_user_Name.text = "${userName}".toLowerCase()
        profile_Name_eT.text = Editable.Factory.getInstance().newEditable(userName)
    }
}