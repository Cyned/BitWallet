package com.example.bitwallet

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import TransactionsModel
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat.startActivity
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_transactions.*
import java.text.SimpleDateFormat
import java.util.*

class Transactions : Activity() {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "token"
    private val PREF_EXCHANGE = "exchange"
    private lateinit var sharedPref: SharedPreferences

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val token: String = sharedPref.getString(PREF_NAME, "").toString()

        val list = findViewById<LinearLayout>(R.id.list)
        getTransactions(token=token, listView = list)

        val homeBtn = findViewById<View>(R.id.home) as ImageView
        homeBtn.setOnClickListener(View.OnClickListener { view ->
            goHome()
        })

    }

    private fun getTransactions(token: String, listView: LinearLayout){
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(URL)
            .build()
        val messageAPI = retrofit.create(ApiService::class.java)
        val call: Call<TransactionsModel> = messageAPI.getTransactions(token)

        call.enqueue(object : Callback<TransactionsModel> {

            override fun onFailure(call: Call<TransactionsModel>?, t: Throwable?) {
                Log.d("FAIL1", t.toString())
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<TransactionsModel>?, response: Response<TransactionsModel>?) {
                if(response?.body() != null) {
                    if(response.isSuccessful and (response.body()!!.status == "success")) {
                        val transactions: List<Map<String, out String>> = response.body()!!.txs
                        Log.d("Status", "Get transactions")
                        var trans_id: Int = 1
                        for(trans in transactions.reversed()) {
                            generateView(trans, id=trans_id, listView=listView)
                            trans_id++
                        }
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

    private fun generateView(transaction: Map<String, out String>, id: Int, listView: LinearLayout) {

        val trans: LinearLayout = LinearLayout(this)
        val status: LinearLayout = LinearLayout(this)
        val time: TextView = TextView(this)
        val statusMsg: TextView = TextView(this)
        val amount: LinearLayout = LinearLayout(this)
        val amountL: LinearLayout = LinearLayout(this)
        val amountBtc: TextView = TextView(this)
        val amountImage: ImageView = ImageView(this)
        val amountDollar: TextView = TextView(this)

        trans.orientation = LinearLayout.HORIZONTAL
        var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, // This will define text view width
            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
        )
        params.setMargins(0, 2, 0, 2)
        trans.layoutParams = params
        trans.setBackgroundColor(resources.getColor(R.color.mainBackground))
        trans.setPadding(8, 4, 8, 4)
//        trans.id = "@+id/trans"

        status.orientation = LinearLayout.VERTICAL
        params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
            ViewGroup.LayoutParams.MATCH_PARENT, // This will define text view height
            0.5f
        )
        status.layoutParams = params
        status.setPadding(4, 4, 4, 4)
//        status.id="@+id/status"

        time.text = getDateTime(transaction["time"].toString())
        params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, // This will define text view width
            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
        )
        time.layoutParams = params
        time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
        time.setTextColor(resources.getColor(R.color.colorAccent))
//        time.setTextAppearance(R.style.CommonUse)
//        time.id="@+id/time"

        statusMsg.text = "via " + transaction["address"].toString().slice(IntRange(0, 15)) + "..."
        params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view height
            1f
        )
        statusMsg.layoutParams = params
        statusMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
        statusMsg.setTextColor(resources.getColor(R.color.authComments))
//        statusMsg.setTextAppearance(R.style.SilentHill)
//        statusMsg.id="@+id/statusMsg"

        amount.orientation = LinearLayout.VERTICAL
        params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
            ViewGroup.LayoutParams.MATCH_PARENT, // This will define text view height
            0.5f
        )
        amount.layoutParams = params
        amount.setPadding(4, 4, 4, 4)
//        amount.id="@+id/amount"

        amountL.orientation = LinearLayout.HORIZONTAL
        params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, // This will define text view width
            ViewGroup.LayoutParams.MATCH_PARENT, // This will define text view height
            0.5f
        )
        amountL.layoutParams = params
//        amount.id="@+id/amountL"

        amountBtc.text = String.format("%.4f", transaction["amount"]!!.toFloat()) + " BTC"
        params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, // This will define text view width
            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view height
            0.2f
        )
        params.setMargins(4,0,4,0)
        amountBtc.layoutParams = params
        amountBtc.gravity = Gravity.END
        amountBtc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
//        amountBtc.id="@+id/amountBtc"

        params = LinearLayout.LayoutParams(
            (100 * Resources.getSystem().getDisplayMetrics().density).toInt(), // This will define text view width
            (21 * Resources.getSystem().getDisplayMetrics().density).toInt(), // This will define text view height
            0.8f
        )
        amountImage.layoutParams = params
        amountImage.setPadding(4,2,4,2)
        amount.gravity = Gravity.END
//        android:id="@+id/picture"


        if (transaction["category"] == "receive") {
            amountBtc.setTextColor(resources.getColor(R.color.deltaUp))
            amountImage.setBackgroundResource(R.drawable.up)
        }else{
            amountBtc.setTextColor(resources.getColor(R.color.deltaDown))
            amountImage.setBackgroundResource(R.drawable.down)
        }

        sharedPref = getSharedPreferences(PREF_EXCHANGE, PRIVATE_MODE)
        val price: Float = sharedPref.getFloat(PREF_EXCHANGE, 0.0f)
        amountDollar.text = String.format("%.2f", transaction["amount"].toString().toFloat() * price) + "$"
        params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, // This will define text view width
            ViewGroup.LayoutParams.MATCH_PARENT, // This will define text view height
            0.5f
        )
        amountBtc.layoutParams = params
        amountDollar.gravity = Gravity.END
        amountDollar.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
        amountDollar.setTextColor(resources.getColor(R.color.authComments))
//        amountDollar.id="@+id/amountDollar"

        status.addView(time)
        status.addView(statusMsg)
        amount.addView(amountL)
        amount.addView(amountDollar)
        amountL.addView(amountBtc)
        amountL.addView(amountImage)
        trans.addView(status)
        trans.addView(amount)
        listView.addView(trans)
    }

    private fun goHome() {
        startActivity(Intent(this, Wallet::class.java))
    }

    private fun getDateTime(s: String): String? {
        try {
            val sdf = SimpleDateFormat("MM/dd/yyyy H:m")
            val netDate = Date(s.toLong() * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

}
