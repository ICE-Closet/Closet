package com.example.smartice_closet.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.smartice_closet.R
import com.example.smartice_closet.auth.google.googleRequest
import com.example.smartice_closet.auth.google.googleResponse
import com.example.smartice_closet.auth.kakao.SessionCallback
import com.example.smartice_closet.main
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.auth.AuthType
import com.kakao.auth.Session
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class login : AppCompatActivity() {

    private val USERNAME = "USERNAME"
    private val TOKEN = "USERTOKEN"

    var userToken: String = ""
    var name: String = ""

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    val RC_SIGN_IN = 9001

    private var callback : SessionCallback = SessionCallback(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signUp_Tbtn = findViewById(R.id.signUp) as TextView
//        val password_Tbtn = findViewById(R.id.forgotPwd) as TextView  // Not Implemented

        auth = FirebaseAuth.getInstance()

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        google_login_btn.setOnClickListener { googleLogin() }

        kakao_login_btn.setOnClickListener { kakaoLogin() }

        login_btn.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            var loginservice: loginRequest = retrofit.create(loginRequest::class.java)

            var s_email = inputEmail.text.toString()
            var s_pw = inputPwd.text.toString()

            loginservice.requestLogin(s_email, s_pw).enqueue(object : Callback<loginResponse> {
                override fun onFailure(call: Call<loginResponse>, t: Throwable) {
                    Log.e("Login", t.message)
                    var dialog = AlertDialog.Builder(this@login)
                    dialog.setTitle("ERROR")
                    dialog.setMessage("서버와의 통신에 실패하였습니다.")
                    dialog.show()
                }

                override fun onResponse(call: Call<loginResponse>, response: Response<loginResponse>) {
                    if (response?.isSuccessful) {
                        if (response.code() == 200) {
                            Log.d("Status 200", "Success")

                            val login_response = response.body()
                            Log.d("STATUS 200", login_response.toString())
//                            var code = login_response?.code?.let { it -> Integer.parseInt(it) }

                            Toast.makeText(this@login, "로그인에 성공하였습니다. \n 즐거운 하루 되세요.", Toast.LENGTH_SHORT).show()
                            var intent = Intent(applicationContext, main::class.java).apply {
                                putExtra(TOKEN, login_response?.token)
                                putExtra(USERNAME, login_response?.name)
                            }
                            startActivity(intent)
                            finish()
                        }
                    }
                    else {
                        if (response.code() == 400) {
                            Log.d("Status 400", response.errorBody().toString())
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            Log.d("errorBody", jsonObject.toString())
                            var cod: String = jsonObject.getString("code")
                            val msg: String = jsonObject.getString("msg")

                            var code = cod?.let { it -> Integer.parseInt(it) }

                            if (code == 1) {
                                Log.d("Status 400 - Code 1", msg)
                                Toast.makeText(this@login, "로그인에 실패하였습니다. \n 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                            }
                            else if (code == 2) {
                                Log.d("Status 400 - Code 2", msg)
                                Toast.makeText(this@login, "회원가입 후 로그인 해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else if (response.code() == 401) {
                            Log.d("Status 401", response.errorBody().toString())
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            Log.d("errorBody", jsonObject.toString())
                            val msg: String = jsonObject.getString("msg")

                            Log.d("Status 401", msg)
                            Toast.makeText(this@login, "이메일이 인증되지 않았습니다. \n 입력하신 이메일로 인증해 주세요.", Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            })

        }

        signUp_Tbtn.setOnClickListener {
            var intent = Intent(this, signup::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun kakaoLogin() {
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.i("Log", "session get current session")
            return
        }

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result != null) {
                if (result.isSuccess) {
                    val account = result.signInAccount
                    firebaseAuthWithGoogle(account!!)
                }
            }
        }
    }

    private fun googleLogin() {
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    sendToServerGoogle(user)
                }
            }

    }

    private fun sendToServerGoogle(user: FirebaseUser?) {
        val googleRetrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val googleLoginRequest: googleRequest = googleRetrofit.create(googleRequest::class.java)

        val user = FirebaseAuth.getInstance().currentUser
        Log.d("sendToServer", user.toString())

        var uid = ""
        var email = ""

        if (user != null) {
            uid = user.uid
            email = user.email.toString()
        }
        else {
            Log.d("sendToServer", "No Current User")
        }

        googleLoginRequest.requestGoogleLogin(uid = uid, email = email).enqueue(object : Callback<googleResponse> {
            override fun onFailure(call: Call<googleResponse>, t: Throwable) {
                Log.e("onFailure", t.message)
                var dialog = AlertDialog.Builder(this@login)
                dialog.setTitle("ERROR")
                dialog.setMessage("서버와의 통신에 실패하였습니다.")
                dialog.show()
            }

            override fun onResponse(call: Call<googleResponse>, response: Response<googleResponse>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        Log.d("Status 200", "Success")
                        var google_Response = response.body()

                        Toast.makeText(this@login, "로그인에 성공하였습니다. \n 즐거운 하루 되세요.", Toast.LENGTH_SHORT).show()
                        Log.d("onResponse", google_Response?.msg)
                        Log.d("onResponse", google_Response?.name)
                        Log.d("onResponse", google_Response?.token)

                        var intent = Intent(applicationContext, main::class.java).apply {
                            putExtra(TOKEN, google_Response?.token)
                            putExtra(USERNAME, google_Response?.name)
                        }
                        startActivity(intent)
                        finish()
                    }
                }
                else {
                    if (response.code() == 400) {
                        Toast.makeText(this@login, "잠시 후 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

    }

//    override fun onStart() {
//        super.onStart()
//
//        sendToServerGoogle(auth?.currentUser)
//    }

}