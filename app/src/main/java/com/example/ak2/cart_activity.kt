package com.example.ak2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ak2.Adapters.Buy_now_adapter
import com.example.ak2.Adapters.cart_adapter
import com.example.ak2.Models.Buy_now_grid
import com.example.ak2.Models.cart_grid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_cart_activity.*
import java.lang.Exception

class cart_activity : AppCompatActivity() {
    private var cart_recycler: RecyclerView? = null
    private var cart_griditem: ArrayList<cart_grid>? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var cart_adapter: cart_adapter? = null
    var total:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar?.title = "My cart"
        setContentView(R.layout.activity_cart_activity)
        cart_recycler= findViewById(R.id.cart_recycler)
        cart_griditem = ArrayList<cart_grid>()
        val  dbRef = FirebaseDatabase.getInstance().getReference().child("Cart").child("Users")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                cart_griditem!!.clear()
                for (ds: DataSnapshot in p0.children)
                {
                    for (ds1:DataSnapshot in ds.children)
                    {
                        try {
                            var c = cart_grid()
                            c.Product_description = ds1.child("Product_description").value.toString()
                            c.Product_name = ds1.child("Product_name").value.toString()
                            c.Product_Price = ds1.child("Product_Price").value.toString()
                            c.url = ds1.child("url").value.toString()
                            c.Product_id = ds1.child("Product_id").value.toString()
                            c.category=ds1.child("category").value.toString()
                            cart_griditem!!.add(c)
                            Log.d("buy", ds1.child("product_description").value.toString())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    cart_adapter!!.update(cart_griditem!!)
                    total=cart_adapter!!.ToatalPrice()
                    total_price.setText(total.toString())


                }
            }
        })
        gridLayoutManager = GridLayoutManager(applicationContext, 1, LinearLayoutManager.VERTICAL, false)
        cart_recycler?.layoutManager = gridLayoutManager
        cart_recycler?.setHasFixedSize(true)
        cart_adapter= cart_adapter(this,cart_griditem!!)
        cart_recycler?.adapter= cart_adapter

        val proceedbtn=findViewById<Button>(R.id.proceed)
        proceedbtn.setOnClickListener {
           /* var prodid = getIntent().getExtras()?.get("prodid").toString()*/
            val  dbRef = FirebaseDatabase.getInstance().getReference().child("Cart").child("Users")

            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(p0: DataSnapshot) {
                    cart_griditem!!.clear()
                    for (ds: DataSnapshot in p0.children)
                    {
                        for (ds1:DataSnapshot in ds.children)
                        {
                            try {
                               /* c.Product_description = ds1.child("Product_description").value.toString()
                                c.Product_name = ds1.child("Product_name").value.toString()
                                c.Product_Price = ds1.child("Product_Price").value.toString()
                                c.url = ds1.child("url").value.toString()*/
                               /* c.Product_id = ds1.child("Product_id").value.toString()*/
                               /* c.category=ds1.child("category").value.toString()*/

                                val prodids=ds1.child("Product_id").value.toString()
                                    Log.d("proicart",prodids.toString())
                                val intent= Intent(applicationContext,confirm_order_activity::class.java)
                                intent.putExtra("totalprice",total.toString())
                                intent.putExtra("prod_id",prodids)
                                it.context.startActivity(intent)
                                Log.d("buy", ds1.child("product_description").value.toString())
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })

        }
    }
    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser == null){
            Toast.makeText(this,"Please login to go further",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Register::class.java))
            finish()
        }
    }
}
