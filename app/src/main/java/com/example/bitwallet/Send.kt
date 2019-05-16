package com.example.bitwallet

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import SendModel
import android.widget.Toast


class Send : Activity() {

    private var PRIVATE_MODE = 0
    private val PREF_TOKEN = "token"
    private val PREF_BALANCE = "balance"
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        sharedPref = getSharedPreferences(PREF_TOKEN, PRIVATE_MODE)
        val token: String = sharedPref.getString(PREF_TOKEN, "").toString()
        val balance: Float = sharedPref.getFloat(PREF_BALANCE, 0.0f)

        val homeBtn = findViewById<View>(R.id.home) as ImageView
        homeBtn.setOnClickListener(View.OnClickListener {
                view -> goHome ()
        })

        val sendBtn = findViewById<View>(R.id.send) as Button
        sendBtn.setOnClickListener(View.OnClickListener {
                view -> send (token = token)
        })

        val availableView = findViewById<View>(R.id.availableNumber) as TextView
        availableView.text = balance.toString()

        val amountView = findViewById<View>(R.id.amountInput) as TextView
        val b0001 = findViewById<View>(R.id.b0001) as Button
        val b0010 = findViewById<View>(R.id.b0010) as Button
        val b0100 = findViewById<View>(R.id.b0100) as Button
        val b1000 = findViewById<View>(R.id.b1000) as Button
        val bmax  = findViewById<View>(R.id.bmax) as Button
        b0001.setOnClickListener(View.OnClickListener {
                view -> setAmount (0.0001f, amountView)
        })
        b0010.setOnClickListener(View.OnClickListener {
                view -> setAmount (0.001f, amountView)
        })
        b0100.setOnClickListener(View.OnClickListener {
                view -> setAmount (0.01f, amountView)
        })
        b1000.setOnClickListener(View.OnClickListener {
                view -> setAmount (0.1f, amountView)
        })
        bmax.setOnClickListener(View.OnClickListener {
                view -> setAmount (balance, amountView)
        })

    }

    private fun goHome() {
        startActivity(Intent(this, Wallet::class.java))
    }

    private fun send(token: String) {

        val amountView = findViewById<View>(R.id.amountInput) as TextView
        val commentView = findViewById<View>(R.id.memoInput) as TextView
        val addressView = findViewById<View>(R.id.toUser) as TextView
        val amount: Float = amountView.text.toString().toFloat()
        val comment: String= commentView.text.toString()
        val address: String = addressView.text.toString()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(URL)
            .build()
        val messageAPI = retrofit.create(ApiService::class.java)
        val call: Call<SendModel> = messageAPI.send(token=token, address=address, amount=amount, comment=comment)
        Log.d("Status", amount.toString())
        Log.d("Status", comment)
        Log.d("Status", address)
        Log.d("Status", token)
        call.enqueue(object : Callback<SendModel> {

            override fun onFailure(call: Call<SendModel>?, t: Throwable?) {
                Log.d("FAIL1", t.toString())
            }

            override fun onResponse(call: Call<SendModel>?, response: Response<SendModel>?) {
                if(response?.body() != null) {
                    if(response.isSuccessful) {
                        if(response.body()!!.status == "success"){
                            goSuccess()
                        }else{
                            goFail()
                        }
                    }
                    else {
                        Log.d("CODE1", response.body()!!.message)
                        toastFail(response.body()!!.message)
                    }
                }else{
                    Log.d("CODE1", "CODE IS $response.code()")
                    toastFail("Invalid credentials :|")
                }
            }
        })
    }

    private fun toastFail(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun goSuccess(){
        startActivity(Intent(this, Success::class.java))
    }

    private fun goFail(){
        startActivity(Intent(this, Fail::class.java))
    }

    private fun setAmount(amount: Float, amountView: TextView){
        amountView.text = amount.toString()
    }
}
