package com.example.bitwallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import LoginModel
import android.content.SharedPreferences
import android.widget.ImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Login : Activity(){

    val mAuth = FirebaseAuth.getInstance()
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "token"
    private lateinit var sharedPref: SharedPreferences;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        val loginBtn = findViewById<ImageView>(R.id.login)
        loginBtn.setOnClickListener(View.OnClickListener {
            view -> login()
        })

        val registerBtn = findViewById<View>(R.id.registerBtn) as TextView
        registerBtn.setOnClickListener(View.OnClickListener {
            view -> register()
        })

        val mailBtn = findViewById<ImageView>(R.id.mailBtn)
        val mailEditText = findViewById<View>(R.id.mailInput) as EditText
        mailBtn.setOnClickListener(View.OnClickListener {
            view -> editTextClean(mailEditText)
        })
    }

    private fun firebaseLogin (email: String, password: String) {
        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful){
                    startActivity(Intent(this, Wallet::class.java))
                    Toast.makeText(this, "Successfully Logged in :)", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error Logging in :(", Toast.LENGTH_LONG).show()
                }
            })
        }else {
            Toast.makeText(this, "Please fill up the Credentials :(", Toast.LENGTH_LONG).show()
        }
    }

    private fun bitwalletLogin(username: String, password: String) : String {

        var token = ""
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val messageAPI = retrofit.create(ApiService::class.java)
        val call: Call<LoginModel> = messageAPI.NewUser(username, password)

        call.enqueue(object : Callback<LoginModel> {

            override fun onFailure(call: Call<LoginModel>?, t: Throwable?) {
                Log.d("FAIL!", t.toString())
            }

            override fun onResponse(call: Call<LoginModel>?, response: Response<LoginModel>?) {
                if(response != null) {
                    if(response.isSuccessful and (response.body()!!.status == "success")) {
                        token = response.body()!!.token
                    }
                    else {
                        Log.d("CODE1", "CODE IS $response.code()")
                    }
                }
            }
        })
        return token
    }

    private fun login() {
        val emailTxt = findViewById<View>(R.id.mailInput) as EditText
        val passwordTxt = findViewById<View>(R.id.passInput) as EditText
        val email = emailTxt.text.toString()
        val password = passwordTxt.text.toString()
        val token: String = bitwalletLogin(username = email, password = password)

        val editor = sharedPref.edit()
        editor.putString(PREF_NAME, token)
        editor.apply()

//        firebaseLogin(email = email, password = password)
        startActivity(Intent(this, Wallet::class.java))
    }

    private fun register() {
        startActivity(Intent(this, com.example.bitwallet.Register::class.java))
    }

    private fun editTextClean(editText: EditText){
        editText.text.clear()
    }

    private fun showResult(token: String){
        Toast.makeText(this, token, Toast.LENGTH_LONG).show()
    }

    private fun showError(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

}
