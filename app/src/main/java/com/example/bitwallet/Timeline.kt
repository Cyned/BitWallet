package com.example.bitwallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Timeline : Activity() {

    val mAuth = FirebaseAuth.getInstance()
    lateinit var mDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        val dispTxt = findViewById<View>(R.id.dispTxt) as TextView

        mDatabase = FirebaseDatabase.getInstance().getReference("Users")

        mDatabase.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                System.out.println("The read failed: ")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.child("Email").toString()
                dispTxt.text = "Welcome $result"
            }

        })

        val signOut = findViewById<View>(R.id.signOut) as Button
        signOut.setOnClickListener(View.OnClickListener {
                view -> signOut()
        })

    }

    private fun signOut(){
        mAuth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
        Toast.makeText(this, "Logged out :(", Toast.LENGTH_LONG).show()
    }

}
