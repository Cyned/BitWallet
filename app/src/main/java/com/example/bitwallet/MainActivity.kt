package com.example.bitwallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginTxt = findViewById<View>(R.id.login) as TextView
        val registerTxt = findViewById<View>(R.id.register) as TextView

        loginTxt.setOnClickListener(View.OnClickListener {
                view -> login ()
        })
        registerTxt.setOnClickListener(View.OnClickListener {
                view -> register ()
        })

    }

    private fun login() {
        startActivity(Intent(this, Login::class.java))
    }

    private fun register() {
        startActivity(Intent(this, Register::class.java))
    }

}
