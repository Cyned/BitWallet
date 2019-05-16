package com.example.bitwallet

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import GetAddressModel
import android.graphics.Bitmap
import android.widget.Button
import android.widget.Toast
import net.glxn.qrgen.android.QRCode
import android.R.attr.label
import android.annotation.SuppressLint
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.support.v4.content.ContextCompat.getSystemService



class Receive : Activity() {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "token"
    private lateinit var sharedPref: SharedPreferences

    private var userAddress: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive)

        sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val token: String = sharedPref.getString(PREF_NAME, "").toString()

        val userCode = findViewById<View>(R.id.userCode) as TextView
        val qrcode = findViewById<View>(R.id.qrcode) as ImageView

        getAddress(token=token, addressText = userCode, addressImage = qrcode)

        val homeBtn = findViewById<View>(R.id.home) as ImageView
        homeBtn.setOnClickListener(View.OnClickListener {
                view -> goHome ()
        })

        val copyBtn = findViewById<View>(R.id.copyBtn) as TextView
        copyBtn.setOnClickListener(View.OnClickListener {
                view -> copy ()
        })
    }

    private fun getAddress(token: String, addressText: TextView, addressImage: ImageView){
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(URL)
            .build()
        val messageAPI = retrofit.create(ApiService::class.java)
        val call: Call<GetAddressModel> = messageAPI.getAddress(token)

        call.enqueue(object : Callback<GetAddressModel> {

            override fun onFailure(call: Call<GetAddressModel>?, t: Throwable?) {
                Log.d("FAIL1", t.toString())
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<GetAddressModel>?, response: Response<GetAddressModel>?) {
                if(response?.body() != null) {
                    if(response.isSuccessful and (response.body()!!.status == "success")) {
                        val address: String = response.body()!!.address
                        Log.d("Status", "Get address: $address")
                        Log.d("Status", "Length ${address.length}")
                        userAddress = address
                        addressText.text = userAddress.slice(IntRange(start=0, endInclusive=10)) +
                                "..." +
                                userAddress.slice(IntRange(start=userAddress.length-11, endInclusive=userAddress.length-1))
                        addressImage.setImageBitmap(generateQR("bitcoin:$address"))
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

    private fun generateQR(text: String) : Bitmap {
        val bitmap = QRCode.from(text).withSize(500, 500).bitmap()
        return bitmap
    }

    private fun toastFail(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun goHome() {
        startActivity(Intent(this, Wallet::class.java))
    }

    private fun copy() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Address", userAddress)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Copied", Toast.LENGTH_LONG).show()
    }

}
