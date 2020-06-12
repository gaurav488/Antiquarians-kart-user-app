package com.example.ak2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ak2.Adapters.cart_adapter
import com.example.ak2.Adapters.ordered_prod_adapater
import com.example.ak2.Models.cart_grid
import com.example.ak2.Models.ordered_grid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_cart_activity.*
import java.lang.Exception

class ordered_products : AppCompatActivity() {
    private var ordered_prod_recycler: RecyclerView? = null
    private var ordered_prod_griditem: ArrayList<ordered_grid>? = null
    private var ordered_prod_adapater: ordered_prod_adapater? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordered_products)
        ordered_prod_recycler= findViewById(R.id.ordere_prods_recycle)
        ordered_prod_griditem = ArrayList<ordered_grid>()
        val uid= FirebaseAuth.getInstance().currentUser?.uid.toString()
        val  dbRef = FirebaseDatabase.getInstance().getReference().child("orders").child(uid)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                ordered_prod_griditem!!.clear()
                for (ds: DataSnapshot in p0.children)
                {
                        try {
                            var c = ordered_grid()
                            c.product_description = ds.child("Product_description").value.toString()
                            c.product_name = ds.child("Product_name").value.toString()
                            c.Product_Price = ds.child("Product_Price").value.toString()
                            c.product_id = ds.child("Product_id").value.toString()
                            c.category=ds.child("category").value.toString()
                            c.orderers_address=ds.child("orderers_address").value.toString()
                            c.orderers_cityname=ds.child("orderers_cityname").value.toString()
                            c.orderers_name=ds.child("orderers_name").value.toString()
                            c.orderers_phone=ds.child("orderers_phone").value.toString()
                            c.url  = ds.child("url").value.toString()
                            Log.d("urlsad",c.url.toString())
                            ordered_prod_griditem!!.add(c)
                            Log.d("buy", ds.child("product_description").value.toString())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    ordered_prod_adapater!!.update(ordered_prod_griditem!!)

            }
        })
        ordered_prod_recycler?.layoutManager=LinearLayoutManager(this@ordered_products)
        ordered_prod_recycler?.setHasFixedSize(true)
       ordered_prod_adapater = ordered_prod_adapater(this,ordered_prod_griditem!!)
        ordered_prod_recycler?.adapter=ordered_prod_adapater
    }
}
