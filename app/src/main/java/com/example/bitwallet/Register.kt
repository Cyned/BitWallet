package com.example.bitwallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class Register : Activity() {

    val mAuth = FirebaseAuth.getInstance()
//    lateinit var mDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val regBtn = findViewById<View>(R.id.register) as Button
        regBtn.setOnClickListener(View.OnClickListener {
            view -> register ()
        })

        val loginTxt = findViewById<View>(R.id.login) as TextView
        loginTxt.setOnClickListener(View.OnClickListener {
                view -> login ()
        })

        val mailBtn = findViewById<View>(R.id.mailBtn) as Button
        val mailEditText = findViewById<View>(R.id.mailInput) as EditText
        mailBtn.setOnClickListener(View.OnClickListener {
                view -> editTextClean(mailEditText)
        })

        val passBtn = findViewById<View>(R.id.passBtn) as Button
        val passEditText = findViewById<View>(R.id.passInput) as EditText
        passBtn.setOnClickListener(View.OnClickListener {
                view -> editTextClean(passEditText)
        })
    }

    private fun register() {
        val emailTxt = findViewById<View>(R.id.mailInput) as EditText
        val passwordTxt = findViewById<View>(R.id.passInput) as EditText
        val repPasswordTxt = findViewById<View>(R.id.repPassInput) as EditText

        val email = emailTxt.text.toString()
        val password = passwordTxt.text.toString()
        val repPassword = repPasswordTxt.text.toString()

        if (!email.isEmpty() && !password.isEmpty() && !repPassword.isEmpty()) {
            if (password == repPassword) {
                mAuth
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
//                            val user = mAuth.currentUser
//                            val uid = user!!.uid
//                            mDatabase.child(uid).child("Email").setValue(email)
//                            mDatabase.child(uid).child("Password").setValue(password)
                            startActivity(Intent(this, Wallet::class.java))
                            Toast.makeText(this, "Successfully registered :)", Toast.LENGTH_LONG).show()
                        } else
                            System.err.println("createUserWithEmail:failure" + task.exception)
                            Toast.makeText(this, "Error registering, try again later :(", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Passwords are different :|", Toast.LENGTH_LONG).show()
            }
        }else {
            Toast.makeText(this,"Please enter up the Credentials :|", Toast.LENGTH_LONG).show()
        }

    }

    private fun login(){
        startActivity(Intent(this, Login::class.java))
    }

    private fun editTextClean(editText: EditText){
        editText.text.clear()
    }

}
