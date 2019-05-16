package com.example.bitwallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import LoginModel
import android.content.SharedPreferences


class Register : Activity() {

    val mAuth = FirebaseAuth.getInstance()
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "token"
    private lateinit var sharedPref: SharedPreferences;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        val regBtn = findViewById<ImageView>(R.id.register)
        regBtn.setOnClickListener(View.OnClickListener {
            view -> register ()
        })

        val loginTxt = findViewById<View>(R.id.login) as TextView
        loginTxt.setOnClickListener(View.OnClickListener {
                view -> login ()
        })

        val mailBtn = findViewById<ImageView>(R.id.mailBtn)
        val mailEditText = findViewById<View>(R.id.mailInput) as EditText
        mailBtn.setOnClickListener(View.OnClickListener {
                view -> editTextClean(mailEditText)
        })

        val passBtn = findViewById<ImageView>(R.id.passBtn)
        val passEditText = findViewById<View>(R.id.passInput) as EditText
        passBtn.setOnClickListener(View.OnClickListener {
                view -> editTextClean(passEditText)
        })
    }

    private fun firebaseRegister(email: String, password: String) {
        mAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Status", "Firebase succesfully registered.")
                    bitwalletRegister(username = email, password = password)
                } else
                    Log.d("Error", "createUserWithEmail:failure" + task.exception)
                    toastFail("Error while registering :(")
            }
    }

    private fun bitwalletRegister(username: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val messageAPI = retrofit.create(ApiService::class.java)
        val call: Call<LoginModel> = messageAPI.register(username, password)

        call.enqueue(object : Callback<LoginModel> {

            override fun onFailure(call: Call<LoginModel>?, t: Throwable?) {
                Log.d("FAIL!", t.toString())
            }

            override fun onResponse(call: Call<LoginModel>?, response: Response<LoginModel>?) {
                if(response?.body() != null) {
                    if(response.isSuccessful and (response.body()!!.status == "success")) {
                        val token: String = response.body()!!.token
                        Log.d("Status", "Receive token: $token")
                        // Save token to memory
                        val editor = sharedPref.edit()
                        editor.putString(PREF_NAME, token)
                        editor.apply()
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

    private fun register() {
        val emailTxt = findViewById<View>(R.id.mailInput) as EditText
        val passwordTxt = findViewById<View>(R.id.passInput) as EditText
        val repPasswordTxt = findViewById<View>(R.id.repPassInput) as EditText

        val email = emailTxt.text.toString()
        val password = passwordTxt.text.toString()
        val repPassword = repPasswordTxt.text.toString()

        if (!email.isEmpty() && !password.isEmpty() && !repPassword.isEmpty() && email.contains("@") && (password.length >=6)) {
            if (password == repPassword) {
                firebaseRegister(email = email, password = password)
            } else {
               toastFail("Passwords are different :|")
            }
        }else {
            toastFail("Please enter up the Credentials :|")
        }
    }

    private fun goToWallet() {
        startActivity(Intent(this, Wallet::class.java))
    }

    private fun login(){
        startActivity(Intent(this, Login::class.java))
    }

    private fun editTextClean(editText: EditText){
        editText.text.clear()
    }

}
