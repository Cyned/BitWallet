package com.example.bitwallet

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()

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
    }

    private fun register() {
        val emailTxt = findViewById<View>(R.id.mailInput) as EditText
        val passwordTxt = findViewById<View>(R.id.passInput) as EditText
        val repPasswordTxt = findViewById<View>(R.id.repPassInput) as EditText
//        val nameTxt = findViewById<View>(R.id.nameTxt) as EditText

        var email = emailTxt.text.toString()
        var password = passwordTxt.text.toString()
        var repPassword = repPasswordTxt.text.toString()
//        var name = nameTxt.text.toString()

        if (!email.isEmpty() && !password.isEmpty()){ //&& !name.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val uid = user!!.uid
//                    mDatabase.child(uid).child("Name").setValue(name)
                    startActivity(Intent(this, Timeline::class.java))
                    Toast.makeText(this, "Successfully registered :)", Toast.LENGTH_LONG).show()
                }else
                    Toast.makeText(this, "Error registering, try again later :(", Toast.LENGTH_LONG).show()
            })

        }else {
            Toast.makeText(this,"Please fill up the Credentials :|", Toast.LENGTH_LONG).show()
        }

    }

    private fun login(){
        startActivity(Intent(this, Login::class.java))
    }

}
