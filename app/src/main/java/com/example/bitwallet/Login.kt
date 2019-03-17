package com.example.bitwallet

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class Login : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn = findViewById<View>(R.id.login) as Button
        loginBtn.setOnClickListener(View.OnClickListener {
            view -> login()
        })

        val registerBtn = findViewById<View>(R.id.register) as TextView
        registerBtn.setOnClickListener(View.OnClickListener {
                view -> register()
        })
    }

    private fun login () {
        val emailTxt = findViewById<View>(R.id.mailInput) as EditText
        val passwordTxt = findViewById<View>(R.id.passInput) as EditText
        var email = emailTxt.text.toString()
        var password = passwordTxt.text.toString()

        if (!email.isEmpty() && !password.isEmpty()) {
            this.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful){
                    startActivity(Intent(this, Timeline::class.java))
                    Toast.makeText(this, "Successfully Logged in:", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error Logging in :(", Toast.LENGTH_LONG).show()
                }
            })
        }else {
            Toast.makeText(this, "Please fill up the Creddentials :|", Toast.LENGTH_LONG).show()
        }
    }

    private fun register() {
        startActivity(Intent(this, Register ::class.java))
    }

}
