package com.example.icecloset.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.icecloset.R
import com.example.icecloset.auth.kakao.SessionCallback
import com.example.icecloset.googleRequest
import com.example.icecloset.auth.google.googleResponse
import com.example.icecloset.mainView
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.auth.Session
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class login : AppCompatActivity() {
    private val TOKEN = "USERTOKEN"
    var userToken: String = ""

    private var callback : SessionCallback = SessionCallback()  // Kakao Auth

    // Google Auth
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Session.getCurrentSession().addCallback(callback)   // Kakao Auth

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()




        var retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var loginservice: forLoginService = retrofit.create(forLoginService::class.java)

        login_btn.setOnClickListener {
            var s_email = user_email.text.toString()
            var s_pw = user_pw.text.toString()

            loginservice.requestLogin(s_email,s_pw).enqueue(object :Callback<forLogin>{
                override fun onFailure(call: Call<forLogin>, t: Throwable) {
                    Log.e("Login", t.message)
                    var dialog = AlertDialog.Builder(this@login)
                    dialog.setTitle("ERROR")
                    dialog.setMessage("서버와의 통신에 실패하였습니다.")
                    dialog.show()
                }

                override fun onResponse(call: Call<forLogin>, response: Response<forLogin>) {
                    if (response?.isSuccessful) {
                        var forLogin = response.body()
                        Log.d("SUCCESS", forLogin?.code)
                        val code = forLogin?.code?.let { it1 -> Integer.parseInt(it1) }
                        if (code == 201) {
                            Toast.makeText(this@login, "로그인에 성공하였습니다. \n 즐거운 하루 되세요.", Toast.LENGTH_SHORT).show()
                            var intent = Intent(applicationContext, mainView::class.java).apply {
                                putExtra(TOKEN, forLogin?.token)
                            }
                            startActivity(intent)
                        }
                        else if (code == 0) {
                            Toast.makeText(this@login, "이메일이 인증되지 않았습니다. \n 입력하신 이메일로 인증해 주세요.", Toast.LENGTH_SHORT).show()
                        }
                        else if (code == 1) {
                            Toast.makeText(this@login, "로그인에 실패하였습니다. \n 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                        }
                        else if (code == 2) {
                            Toast.makeText(this@login, "회원가입 후 로그인 해주세요.", Toast.LENGTH_SHORT).show()

                        }
                    }
                    else {
                        var forLogin = response.body()
                        Log.e("FAIL", response.errorBody().toString())
                        Log.d("FAIL", forLogin?.code)
                        Toast.makeText(this@login, "로그인에 실패하였습니다. \n 계정 혹은 비밀번호를 다시 확인하세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        google_login_btn.setOnClickListener { googleLogin() }

        signup_btn.setOnClickListener {
            var intent = Intent(applicationContext, signup::class.java)
            startActivity(intent)
        }
    }

    private fun googleLogin() {
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    companion object {  // Google Auth
        fun getLaunchIntent(from: Context) = Intent(from, login::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    @SuppressLint("MissingSuperCall")      // Kakao Auth
    override fun onDestroy() {
//        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
        super.onDestroy()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Kakao Auth
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.i("Log", "Session get current session")
            return
        }

        // Google Auth
        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result != null) {
                Log.d("onActivityResult",  result.status.toString())

                if (result.isSuccess) {
                    val account = result.signInAccount
                    firebaseAuthWithGoogle(account!!)
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    moveActivity(user)
                }
            }

    }

    private fun moveActivity(user: FirebaseUser?) {
        sendToServerGoogle()
        if (user != null) {
            var intent = Intent(applicationContext, mainView::class.java).apply {
                putExtra(TOKEN, userToken)
            }
            startActivity(intent)
            finish()
        }
    }



    private fun sendToServerGoogle() {
        var googleRetrofit = Retrofit.Builder()
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
                    var googleResponse = response.body()
                    val code = googleResponse?.code?.let { it -> Integer.parseInt(it) }
                    if (code == 503) {
                        Toast.makeText(this@login, "잠시 후 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
                        Log.d("onResponse", googleResponse?.code)
                        Log.d("onResponse", googleResponse?.msg)
                        Log.d("onResponse", googleResponse?.token)
                        userToken = googleResponse?.token.toString()
                    }
                    else if (code == 201) {
                        Toast.makeText(this@login, "로그인에 성공하였습니다. \n 즐거운 하루 되세요.", Toast.LENGTH_SHORT).show()
                        Log.d("onResponse", googleResponse?.code)
                        Log.d("onResponse", googleResponse?.msg)
                        Log.d("onResponse", googleResponse?.token)
                        userToken = googleResponse?.token.toString()
                    }
                }
                else { }
            }

        })
    }
}