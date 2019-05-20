package com.example.bitwallet

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text

class Settings : Activity() {

    private lateinit var sharedPref: SharedPreferences
    val mAuth = FirebaseAuth.getInstance()
    lateinit var mDatabase : DatabaseReference
    private val internalMem = com.example.bitwallet.internalMem()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        sharedPref = getSharedPreferences(internalMem.PREF_NAME, internalMem.PRIVATE_MODE)
        val username: String = sharedPref.getString(internalMem.PREF_USER, "").toString()


        val usernameView = findViewById<View>(R.id.username) as TextView
        usernameView.text = username

        val homeBtn = findViewById<View>(R.id.home) as ImageView
        homeBtn.setOnClickListener(View.OnClickListener {
                view -> goHome ()
        })

        val signOut = findViewById<View>(R.id.signOut) as TextView
        signOut.setOnClickListener(View.OnClickListener {
                view -> signOut()
        })

    }

    private fun signOut(){
        mAuth.signOut()
        startActivity(Intent(this, Login::class.java))
        Toast.makeText(this, "Logged out :(", Toast.LENGTH_LONG).show()
    }

    private fun goHome() {
        startActivity(Intent(this, Wallet::class.java))
    }


}
