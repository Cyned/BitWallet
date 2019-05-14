package com.example.bitwallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class Settings : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val homeBtn = findViewById<View>(R.id.home) as ImageView
        homeBtn.setOnClickListener(View.OnClickListener {
                view -> goHome ()
        })

    }

    private fun goHome() {
        startActivity(Intent(this, Wallet::class.java))
    }
}
