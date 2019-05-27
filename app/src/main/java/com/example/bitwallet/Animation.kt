package com.example.bitwallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle


class Animation : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        val animationView = findViewById<com.airbnb.lottie.LottieAnimationView>(R.id.animation_view)

        animationView.setAnimation("payment_animation.json")
        animationView.playAnimation()
        animationView.loop(true)

        startActivity(Intent(this, Wallet::class.java))

    }
}
