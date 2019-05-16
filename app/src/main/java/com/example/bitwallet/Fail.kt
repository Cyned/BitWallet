package com.example.bitwallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button

class Fail : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)


        val homeBtn = findViewById<View>(R.id.home) as Button
        homeBtn.setOnClickListener(View.OnClickListener {
                view -> goHome ()
        })

    }

    private fun goHome() {
        startActivity(Intent(this, Wallet::class.java))
    }
}
