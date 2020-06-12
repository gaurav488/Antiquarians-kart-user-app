package com.example.ak2

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_auction_item_info.*
import kotlinx.android.synthetic.main.activity_new_product_info.*
import kotlinx.android.synthetic.main.bottom_app_bar.*
import java.text.DateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap
import kotlin.properties.Delegates

class auction_item_info : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auction_item_info)
        val auctid= getIntent().getExtras()?.get("auct_id").toString()
        Log.d("disp",auctid.toString())
        val price= getIntent().getExtras()?.get("item_amt").toString()
        val itemname= getIntent().getExtras()?.get("itemname").toString()
        Log.d("disp99",itemname.toString())
        setSupportActionBar(toolbar3)
        val actionBar = supportActionBar
        actionBar?.title = itemname
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
                Log.d("disp",prodname.toString())
                bid_end_date.setText("Ends on :"+enddate+","+endtime)
                auct_item_name.setText("Name : "+prodname)
                total_price.setText("Start Bid : "+prodprice)
                auct_item_des.setText("Description : "+proddes)
                Glide.with(applicationContext).load(url).dontAnimate()
                    .into(auct_desc_img)
            }
        })
        val bidbtn=findViewById<Button>(R.id.Biding)
        bidbtn.setOnClickListener {
            val auct_db_ref = FirebaseDatabase.getInstance().getReference().child("auction").child(auctid)
            auct_db_ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(p0: DataSnapshot) {

                    var enddate = p0.child("auct_end_date").value.toString()
                    var endtime=p0.child("auct_end_time").value.toString()
                    val intent= Intent(applicationContext,biding_activity::class.java)
                    intent.putExtra("price",price)
                    intent.putExtra("auctid",auctid)
                    Log.d("disp323232",auctid.toString())
                    intent.putExtra("item_n",itemname)
                    Log.d("disp33232",itemname.toString())
                    intent.putExtra("item_edt",enddate)
                    intent.putExtra("item_et",endtime)
                    startActivity(intent)
                }
            })
        }
    }
}
