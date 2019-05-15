package com.example.bitwallet

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import BalanceModel
import ExchangeModel
import android.graphics.Color
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_wallet.*
import org.w3c.dom.Text


class Wallet : Activity() {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "token"
    private lateinit var sharedPref: SharedPreferences
    private var dollarAmount: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val token: String = sharedPref.getString(PREF_NAME, "").toString()
        // Set wallet information
        val amountView = findViewById<View>(R.id.amount) as TextView
        val decimalView = findViewById<View>(R.id.decimal) as TextView
        setBalance(token=token, amountView = amountView, decimalView = decimalView)

        val marketPriceView = findViewById<View>(R.id.marketPrice) as TextView
        val deltaView = findViewById<View>(R.id.delta) as TextView
        val deltaUp = findViewById<ImageView>(R.id.deltaUp)
        val deltaDown = findViewById<ImageView>(R.id.deltaDown)
        val usdView = findViewById<View>(R.id.usd) as TextView
        setExchange(token=token, marketPriceView=marketPriceView, deltaView=deltaView, deltaDown=deltaDown, deltaUp=deltaUp, usdView=usdView)

        // Set button listeners
        val receiveLayout = findViewById<ConstraintLayout>(R.id.receive)
        val sendLayout = findViewById<ConstraintLayout>(R.id.send)
        val transLayout = findViewById<ConstraintLayout>(R.id.transactions)
        val infoLayout = findViewById<ConstraintLayout>(R.id.info)

        receiveLayout.setOnClickListener(View.OnClickListener {
                view -> goReceive()
        })
        sendLayout.setOnClickListener(View.OnClickListener {
                view -> goSend()
        })
        transLayout.setOnClickListener(View.OnClickListener {
                view -> goTransactions()
        })
        infoLayout.setOnClickListener(View.OnClickListener {
                view -> goInfo()
        })

    }

    private fun setBalance(token: String, amountView: TextView, decimalView: TextView){
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(URL)
            .build()
        val messageAPI = retrofit.create(ApiService::class.java)
        val call: Call<BalanceModel> = messageAPI.getBalance(token)

        call.enqueue(object : Callback<BalanceModel> {

            override fun onFailure(call: Call<BalanceModel>?, t: Throwable?) {
                Log.d("FAIL1", t.toString())
            }

            override fun onResponse(call: Call<BalanceModel>?, response: Response<BalanceModel>?) {
                if(response?.body() != null) {
                    if(response.isSuccessful and (response.body()!!.status == "success")) {
                        val balance: String = response.body()!!.balance
                        Log.d("Status", "Receive balance: $balance")
                        dollarAmount = balance.toFloat()
                        amountView.text = balance.slice(IntRange(start=0, endInclusive = 4))
                        decimalView.text = balance.slice(IntRange(start=4, endInclusive = 6))

                    }
                    else {
                        Log.d("CODE1", response.body()!!.message)
                        toastFail(response.body()!!.message)
                    }
                }else{
                    Log.d("CODE1", "CODE IS $response.code()")
                    toastFail("Internal server error")
                }
            }
        })
    }

    private fun setExchange(token: String, marketPriceView: TextView, deltaView: TextView, deltaUp: ImageView, deltaDown: ImageView, usdView: TextView){
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(URL)
            .build()
        val messageAPI = retrofit.create(ApiService::class.java)
        val call: Call<ExchangeModel> = messageAPI.getExchange(token)

        call.enqueue(object : Callback<ExchangeModel> {
            override fun onFailure(call: Call<ExchangeModel>?, t: Throwable?) {
                Log.d("FAIL1", t.toString())
            }

            override fun onResponse(call: Call<ExchangeModel>?, response: Response<ExchangeModel>?) {
                if(response?.body() != null) {
                    if(response.isSuccessful and (response.body()!!.status == "success")) {
                        val price: String = response.body()!!.price
                        var change24h : Float = response.body()!!.change24h.toFloat()
                        Log.d("Status", "Receive price: $price")

                        if(change24h>=0) {
                            deltaView.setTextColor(resources.getColor(R.color.deltaUp))
                            deltaDown.visibility = View.INVISIBLE
                            deltaUp.visibility = View.VISIBLE
                        }else{
                            deltaView.setTextColor(resources.getColor(R.color.deltaDown))
                            deltaUp.visibility = View.INVISIBLE
                            deltaDown.visibility = View.VISIBLE
                            change24h = -change24h
                        }
                        marketPriceView.text = "1 BTC = $$price"
                        deltaView.text =  String.format("%.2f", change24h * 100) + "%"
                        usdView.text = "$" + String.format("%.2f", dollarAmount * price.toFloat())
                    }
                    else {
                        Log.d("CODE1", response.body()!!.message)
                        toastFail(response.body()!!.message)
                    }
                }else{
                    Log.d("CODE1", "CODE IS $response.code()")
                    toastFail("Internal server error")
                }
            }
        })
    }

    private fun toastFail(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun goReceive() {
        startActivity(Intent(this, Receive::class.java))
    }

    private fun goSend() {
        startActivity(Intent(this, Send::class.java))
    }

    private fun goTransactions() {
        startActivity(Intent(this, Transactions::class.java))
    }

    private fun goInfo() {
        startActivity(Intent(this, Settings::class.java))
    }

}
