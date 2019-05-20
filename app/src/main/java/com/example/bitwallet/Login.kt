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
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.widget.ImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Login : Activity(){

    val mAuth = FirebaseAuth.getInstance()
    private lateinit var sharedPref: SharedPreferences
    private val internalMem = com.example.bitwallet.internalMem()

    @SuppressLint("ApplySharedPref")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPref = getSharedPreferences(internalMem.PREF_NAME, internalMem.PRIVATE_MODE)
        // delete previous token
        val editor = sharedPref.edit()
        editor.putString(internalMem.PREF_TOKEN, "")
        editor.commit()

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
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    bitwalletLogin(username = email, password = password)
                } else {
                    Log.d("Error", "loginUserWithEmail:failure" + task.exception)
                    toastFail("Error while logging :(")
                }
            })
    }

    private fun bitwalletLogin(username: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val messageAPI = retrofit.create(ApiService::class.java)
        val call: Call<LoginModel> = messageAPI.login(username, password)

        call.enqueue(object : Callback<LoginModel> {

            override fun onFailure(call: Call<LoginModel>?, t: Throwable?) {
                Log.d("FAIL1", t.toString())
            }

            @SuppressLint("ApplySharedPref")
            override fun onResponse(call: Call<LoginModel>?, response: Response<LoginModel>?) {
                Log.d("Status", response?.body().toString())
                Log.d("Status", response.toString())
                if(response?.body() != null) {
                    if(response.isSuccessful and (response.body()!!.status == "success")) {
                        val token: String = response.body()!!.token
                        Log.d("Status", "Receive token: $token")
                        // Save token to memory
                        val editor = sharedPref.edit()
                        editor.putString(internalMem.PREF_TOKEN, token)
                        editor.putString(internalMem.PREF_USER, username)
                        editor.commit()
                        goToWallet()
                    }
                    else {
                        Log.d("CODE1", response.body()!!.message)
                        toastFail(response.body()!!.message)
                    }
                }else{
                    Log.d("CODE1", "CODE IS $response.code()")
                    toastFail("Invalid credentials :|")
                }
            }
        })
    }

    private fun toastFail(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun login() {
        val emailTxt = findViewById<View>(R.id.mailInput) as EditText
        val passwordTxt = findViewById<View>(R.id.passInput) as EditText
        val email = emailTxt.text.toString()
        val password = passwordTxt.text.toString()

        if (!email.isEmpty() && !password.isEmpty()) {
            firebaseLogin(email = email, password = password)
        }else {
            Toast.makeText(this, "Please fill up the Credentials :(", Toast.LENGTH_LONG).show()
        }
    }

    private fun goToWallet() {
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
