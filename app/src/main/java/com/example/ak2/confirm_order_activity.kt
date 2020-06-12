package com.example.ak2

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ak2.Models.cart_grid
import com.example.ak2.checksum.CheckSumServiceHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import kotlinx.android.synthetic.main.activity_confirm_order_activity.*
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap


class confirm_order_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_order_activity)
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

     val proceedbtn=findViewById(R.id.proceed_payment) as Button
        proceedbtn.setOnClickListener {
            purchasedetails()

        }
    }
    private fun purchasedetails() {
        var prodid = getIntent().getExtras()?.get("prod_id").toString()
        Log.d("proi",prodid.toString())
        val uid= FirebaseAuth.getInstance().currentUser?.uid.toString()
        val orderer_name = confirm_order_name.text.toString().trim()
        val orderes_phone= orderers_Phone_number.text.toString().trim()
        val orderes_address = orderers_address.text.toString().trim()
        val orderes_cityname= orderers_cityname.text.toString().trim()
        val orderdetails: java.util.HashMap<String, Any> = java.util.HashMap<String, Any>()
        if(orderer_name.isEmpty() && orderes_phone.isEmpty() && orderes_address.isEmpty() && orderes_cityname.isEmpty()){
            Toast.makeText(this,"Please enter the details",Toast.LENGTH_SHORT).show()
        }
        if (orderer_name.isNotEmpty()) {
            orderdetails.put("orderers_name",orderer_name)
        }
        if (orderer_name.isNotEmpty()) {
            orderdetails.put("orderers_phone",orderes_phone)
        }
        if (orderer_name.isNotEmpty()) {
            orderdetails.put("orderers_address",orderes_address)
        }
        if (orderer_name.isNotEmpty()) {
            orderdetails.put("orderers_cityname",orderes_cityname)
        }
        val  dbRef = FirebaseDatabase.getInstance().getReference().child("Cart").child("Users").child(uid)
        val orderef=FirebaseDatabase.getInstance().getReference().child("orders")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {

                    for (ds: DataSnapshot in p0.children) {
                       for (ds1: DataSnapshot in ds.children) {
                           //orderdetails.put(ds.key.toString(),ds.value.toString())
                                Log.d("buy", ds.child("product_description").value.toString())
                                val Product_description = ds1.child("Product_description").value.toString()
                                val Product_name = ds1.child("Product_name").value.toString()
                                val Product_Price = ds1.child("Product_Price").value.toString()
                                val url = ds1.child("url").value.toString()
                                val Product_id = ds1.child("Product_id").value.toString()
                                val category = ds1.child("category").value.toString()
                                val useridofprod=ds1.child("user_Id").value.toString()
                                orderdetails.put("Product_description", Product_description)
                                orderdetails.put("Product_name", Product_name)
                                orderdetails.put("Product_Price", Product_Price)
                                orderdetails.put("url", url)
                                orderdetails.put("Product_id", Product_id)
                                orderdetails.put("category", category)
                                ds.key?.let{it1 -> orderef.child(uid).child(it1).updateChildren(orderdetails)}
                     /*      val userprodref= FirebaseDatabase.getInstance().getReference()
                           userprodref.child("Users").child(useridofprod).child("user_product").child(category).child(Product_id).removeValue()
                           val prodtoremoveaftersuccess=FirebaseDatabase.getInstance().getReference().child("new_product")
                           prodtoremoveaftersuccess.child(category).child(Product_id).removeValue()
                           val cartRef = FirebaseDatabase.getInstance().getReference().child("Cart").child("Users").child(uid)
                           cartRef.removeValue()*/
                    }

                }
            }

        })

}
private fun openPaytmInApp() {
var proprice = getIntent().getExtras()?.get("totalprice").toString()
val instance = this
val mContext = applicationContext
val pricetot = proprice.toString().toFloat().toString()
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
   object : PaytmPaymentTransactionCallback {
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

}


/*for (ds: DataSnapshot in p0.children)
 {

     for (ds1:DataSnapshot in ds.children) {
         Log.d("sad", ds.toString())

             try {
             *//*var c = cart_grid()
                   c.Product_description = ds1.child("Product_description").value.toString()
                   c.Product_name = ds1.child("Product_name").value.toString()
                   c.Product_Price = ds1.child("Product_Price").value.toString()
                   c.url = ds1.child("url").value.toString()
                   c.Product_id = ds1.child("Product_id").value.toString()
                   c.category = ds1.child("category").value.toString()*//*
                       Log.d("buy", ds1.child("product_description").value.toString())
                       val Product_description =
                           ds1.child("Product_description").value.toString()
                       val Product_name = ds1.child("Product_name").value.toString()
                       val Product_Price = ds1.child("Product_Price").value.toString()
                       val url = ds1.child("url").value.toString()
                       val Product_id = ds1.child("Product_id").value.toString()
                       val category = ds1.child("category").value.toString()
                       hashMap.put("Product_description", Product_description)
                       hashMap.put("Product_name", Product_name)
                       hashMap.put("Product_Price", Product_Price)
                       hashMap.put("url ", url)
                       hashMap.put("Product_id", Product_id)
                       hashMap.put("category", category)
                       val dbref1 =
                           FirebaseDatabase.getInstance().getReference().child("orders")
                               .child(orderes_phone)
                       dbref1.child(prodid).setValue(hashMap).addOnCompleteListener {
                           Toast.makeText(
                               this@confirm_order_activity, "Order has been placed", Toast.LENGTH_SHORT).show()
                           val cartRef =
                               FirebaseDatabase.getInstance().getReference().child("Cart")
                                   .child("Users")
                           cartRef.child(uid).child(prodid).removeValue()
                       }
                   } catch (e: Exception) {
                       e.printStackTrace()
                   }
               }
           }

       }*/