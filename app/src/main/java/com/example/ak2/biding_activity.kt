package com.example.ak2

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.ak2.checksum.CheckSumServiceHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import kotlinx.android.synthetic.main.activity_auction_item_info.*
import kotlinx.android.synthetic.main.activity_biding_activity.*
//import kotlinx.android.synthetic.main.activity_biding_activity.toolbar
import kotlinx.android.synthetic.main.bottom_app_bar.*
import java.util.*
import kotlin.collections.HashMap

class biding_activity : AppCompatActivity() {
    var priceplaced: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biding_activity)
        var auctid = getIntent().getExtras()?.get("auctid").toString()

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS
                ), 101
            )
        }

        bidder_list.setOnClickListener {
            val intent= Intent(this,bidders_list_activity::class.java)
            intent.putExtra("aucid",auctid)
            startActivity(intent)
        }
        val auct_db_ref = FirebaseDatabase.getInstance().getReference().child("auction").child(auctid)
        auct_db_ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                var prodname = p0.child("item_name").value.toString()
                var proddes = p0.child("item_description").value.toString()
                var url = p0.child("url").value.toString()
                var enddate = p0.child("auct_end_date").value.toString()
                var endtime=p0.child("auct_end_time").value.toString()
                var prodprice = p0.child("item_amt").value.toString()
                var prodestimatedstartamt=p0.child("estimated_start").value.toString()
                var prodestimatedendamt=p0.child("estimated_end").value.toString()
                estimatedstartbid.setText(prodestimatedstartamt+"-")
                estimatedendbid.setText(prodestimatedendamt)
                bid_item_end_date.setText("Ends on :"+enddate+","+endtime)
                bid_item_name.setText("Name : "+prodname)
                bid_item_amt.setText("Start Bid : "+prodprice)
                bid_item_des.setText("Description : "+proddes)
                Glide.with(applicationContext).load(url).dontAnimate()
                    .into(bid_icon_img)
            }
        })
        val bidbtn = findViewById<Button>(R.id.biding_proceed)
        bidbtn.setOnClickListener {
           biddetail()
        }

        val price = getIntent().getExtras()?.get("price").toString()
        price_picker.maxValue = 20000
        price_picker.minValue = price.toInt()
        price_picker.setOnValueChangedListener { picker, oldVal, newVal ->
            if (newVal < oldVal) {
                picker.value = oldVal - 200
            } else {
                picker.value = newVal + 200
            }
            priceplaced = picker.value
        }
    }
    private fun biddetail(){

        var auctid = getIntent().getExtras()?.get("auctid").toString()
        var itname = getIntent().getExtras()?.get("item_n").toString()
        var auct_end_date= getIntent().getExtras()?.get("item_edt").toString()
        var auct_end_time = getIntent().getExtras()?.get("item_et").toString()
        var itprice = getIntent().getExtras()?.get("price").toString()
        val calen = Calendar.getInstance()
        val currentdate = java.text.SimpleDateFormat("dd MMM, yyyy")
        val savecurrent = currentdate.format(calen.time)
        val currenttime = java.text.SimpleDateFormat("hh:mm a")
        val savecurrenttime = currenttime.format(calen.time)
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val userdbref=FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
        userdbref.addListenerForSingleValueEvent(object : ValueEventListener {
             override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                var username=p0.child("username").value.toString()
                val hashMap: HashMap<String, String> = HashMap<String, String>()
                hashMap.put("username",username)
                hashMap.put("Uid", uid.toString())
                hashMap.put("current_date_time", savecurrent.toString()+","+savecurrenttime.toString())
                hashMap.put("bid_amount", priceplaced.toString())
                hashMap.put("auct_id",auctid)
                val auct_db_ref = FirebaseDatabase.getInstance().getReference().child("auction").child(auctid)
                auct_db_ref.child("biding").child(uid).setValue(hashMap)
                val hashmap=HashMap<String,Any>()
                hashmap.put("current_date_time", savecurrent.toString()+","+savecurrenttime.toString())
                hashmap.put("bid_amount", priceplaced.toString())
                hashmap.put("auct_id",auctid)
                hashmap.put("item_name",itname)
                Log.d("inam",itname.toString())
                hashmap.put("item_amt",itprice)
                hashmap.put("auct_end_date",auct_end_date)
                hashmap.put("auct_end_time",auct_end_time)
                val firedb= FirebaseDatabase.getInstance().getReference("Users").child(uid)
                firedb.child("active_bid").child(auctid).setValue(hashmap)
                val pasthist= FirebaseDatabase.getInstance().getReference("Users").child(uid)
                pasthist.child("past_bid").child(auctid).setValue(hashmap)
                openPaytmInApp()
                //startActivity(Intent(this@biding_activity,AuctionTab::class.java))
            }
        })
    }
    private fun openPaytmInApp() {
        val instance = this
        val mContext = applicationContext
        val pricetot = priceplaced.toString().toFloat().toString()
        Log.d("amt", pricetot)
        val Service = PaytmPGService.getStagingService("")
        val orderid = "order" + (0..99999).random()
        val custid = "cust" + (0..99999).random()
        val amount = pricetot
        val paramMap = HashMap<String, String>()
        Log.d("orderid", orderid)
        Log.d("custid", custid)
        paramMap["MID"] = Keys.MID
        paramMap["ORDER_ID"] = orderid
        paramMap["WEBSITE"] = "WEBSTAGING"
        paramMap["INDUSTRY_TYPE_ID"] = "Retail";
        paramMap["CHANNEL_ID"] = "WAP";
        paramMap["CUST_ID"] = custid;
        paramMap["TXN_AMOUNT"] = amount;
        paramMap["CALLBACK_URL"] = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=${orderid}";

        val paramMap2 = TreeMap<String, String>()
        paramMap2.putAll(paramMap)
        val CHECKSUMHASH =
            CheckSumServiceHelper().genrateCheckSum(
                Keys.MID_KEY,
                paramMap2
            )

        paramMap["CHECKSUMHASH"] = CHECKSUMHASH
        val Order = PaytmOrder(paramMap)
        Service.initialize(Order, null);
        Service.startPaymentTransaction(
            instance,
            true,
            true,
            object : PaytmPaymentTransactionCallback{
                override fun onTransactionResponse(inResponse: Bundle?) {
                    Toast.makeText(applicationContext, "Payment Transaction response $inResponse", Toast.LENGTH_SHORT).show()
                }

                override fun clientAuthenticationFailed(inErrorMessage: String?) {
                    Toast.makeText(applicationContext, "Client Authentication Failed due to $inErrorMessage", Toast.LENGTH_SHORT).show()
                }

                override fun someUIErrorOccurred(inErrorMessage: String?) {
                    Toast.makeText(applicationContext, "UI Error Message $inErrorMessage", Toast.LENGTH_SHORT).show()
                }

                override fun onTransactionCancel(inErrorMessage: String?, inResponse: Bundle?) {
                    Toast.makeText(applicationContext, "Transaction Cancelled due to $inErrorMessage", Toast.LENGTH_SHORT).show()
                }

                override fun networkNotAvailable() {
                    Toast.makeText(applicationContext, "Network Not Available ", Toast.LENGTH_SHORT).show()
                }

                override fun onErrorLoadingWebPage(
                    iniErrorCode: Int,
                    inErrorMessage: String?,
                    inFailingUrl: String?
                ) {
                    Toast.makeText(applicationContext, "Payment Transaction response $inErrorMessage", Toast.LENGTH_SHORT).show()
                }

                override fun onBackPressedCancelTransaction() {
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser == null){
            Toast.makeText(applicationContext,"You need to login",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Register::class.java))
            finish()
        }
    }

}
/*
val current= FirebaseDatabase.getInstance().getReference("Users").child(uid)
current.child("past_history").child(auctid).setValue(hashmap)*/
