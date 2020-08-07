package com.example.icecloset.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.icecloset.R
import com.example.icecloset.auth.kakao.SessionCallback
import com.example.icecloset.mainView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.auth.Session
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class login : AppCompatActivity() {
    private val TOKEN = "USERTOKEN"

    private var callback : SessionCallback = SessionCallback()  // Kakao Auth

    // Google Auth
    val RC_SIGN_IN: Int = 1
    lateinit var signInClient: GoogleSignInClient
    lateinit var signInOptions: GoogleSignInOptions
    private lateinit var auth: FirebaseAuth

//    lateinit var google_loginservice: forGoogleLoginService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Session.getCurrentSession().addCallback(callback)   // Kakao Auth


        // Google Auth
        auth = FirebaseAuth.getInstance()
        initializeUI()
        setupGoogleLogin()


        var retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var loginservice: forLoginService = retrofit.create(
            forLoginService::class.java)

//        google_loginservice = retrofit.create(forGoogleLoginService::class.java)

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
                        Log.d("FAIL", forLogin?.code)
                        Toast.makeText(this@login, "로그인에 실패하였습니다. \n 계정 혹은 비밀번호를 다시 확인하세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        signup_btn.setOnClickListener {
            var intent = Intent(applicationContext, signup::class.java)
            startActivity(intent)
        }
    }

    companion object {  // Google Auth
        fun getLaunchIntent(from: Context) = Intent(from, login::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onStart() {    // Google Auth
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            startActivity(mainView.getLaunchIntent(this))
            finish()
        }
    }

    private fun initializeUI() {    // Google Auth
//        var uid = ""
//        var email = ""
//
//        val user = FirebaseAuth.getInstance().currentUser
//        if (user != null) {
//            uid = user.uid
//            Log.d("!!!!!!", uid)
//            email = user.email.toString()
//            Log.d("!!!!!!", email)
//        }
//        else {
//            Log.d("LOG", "No User Data")
//        }

        google_login_btn.setOnClickListener {
//            google_loginservice.requestGoogleLogin(uid = uid, email = email).enqueue(object :Callback<forGoogleLogin> {
//                override fun onFailure(call: Call<forGoogleLogin>, t: Throwable) {
//                    Log.e("Login", t.message)
//                    var dialog = AlertDialog.Builder(this@login)
//                    dialog.setTitle("ERROR")
//                    dialog.setMessage("서버와의 통신에 실패하였습니다.")
//                    dialog.show()
//                }
//
//                override fun onResponse(call: Call<forGoogleLogin>, response: Response<forGoogleLogin>) {
//                    if (response?.isSuccessful) {
//                        var forGoogleLogin = response.body()
//                        Log.d("SUCCESS", forGoogleLogin?.code)
//                        val code = forGoogleLogin?.code?.let { it1 -> Integer.parseInt(it1) }
//                        if (code == 201) {
//                            var intent = Intent(applicationContext, main::class.java).apply {
//                                putExtra(TOKEN, forGoogleLogin?.token)
//                            }
//                            startActivity(intent)
//                        }
//                        else {
//                            Toast.makeText(this@login, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                    else {
//                        var forGoogleLogin = response.body()
//                        Log.d("FAIL", forGoogleLogin?.code)
//                    }
//                }
//            })
            login()
        }
    }

    private fun login() {   // Google Auth
        val loginIntent: Intent = signInClient.signInIntent
        startActivityForResult(loginIntent, RC_SIGN_IN)
    }


    @SuppressLint("MissingSuperCall")      // Kakao Auth
    override fun onDestroy() {
//        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Kakao Auth
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.i("Log", "Session get current session")
            return
        }

        // Google Auth
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    googleFirebaseAuth(account)
                }
            } catch (e: ApiException) {
                Toast.makeText(this@login, "Google 로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // Google Auth
    private fun googleFirebaseAuth(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                startActivity(mainView.getLaunchIntent(this))
//                val user = FirebaseAuth.getInstance().currentUser
//                user?.let {
//                    for (profile in it.providerData) {
//                        val uid = profile.uid
//                        Log.d("UID", uid)
//                        val email = profile.email
//                        Log.d("EMAIL", email)
//                    }
//                }
            }
            else {
                Toast.makeText(this@login, "Google 로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Google Auth
    private fun setupGoogleLogin() {
        signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        signInClient = GoogleSignIn.getClient(this, signInOptions)
    }
}