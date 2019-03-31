package com.example.bitwallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login : Activity() {

    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn = findViewById<View>(R.id.login) as Button
        loginBtn.setOnClickListener(View.OnClickListener {
            view -> login()
        })

        val registerBtn = findViewById<View>(R.id.registerBtn) as TextView
        registerBtn.setOnClickListener(View.OnClickListener {
            view -> register()
        })

        val mailBtn = findViewById<View>(R.id.mailBtn) as Button
        val mailEditText = findViewById<View>(R.id.mailInput) as EditText
        mailBtn.setOnClickListener(View.OnClickListener {
            view -> editTextClean(mailEditText)
        })
    }

    private fun login () {
        val emailTxt = findViewById<View>(R.id.mailInput) as EditText
        val passwordTxt = findViewById<View>(R.id.passInput) as EditText
        val email = emailTxt.text.toString()
        val password = passwordTxt.text.toString()


        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful){
                    startActivity(Intent(this, Timeline::class.java))
                    Toast.makeText(this, "Successfully Logged in:", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error Logging in :(", Toast.LENGTH_LONG).show()
                }
            })
        }else {
            Toast.makeText(this, "Please fill up the Credentials :(", Toast.LENGTH_LONG).show()
        }
    }

    private fun register() {
        startActivity(Intent(this, Register::class.java))
    }

    private fun editTextClean(editText: EditText){
        editText.text.clear()
    }

}
