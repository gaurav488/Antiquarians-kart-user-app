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
import com.example.ak2.Adapters.my_product_adapter
import com.example.ak2.Models.cart_grid
import com.example.ak2.Models.my_product_grid
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_cart_activity.*
import java.lang.Exception

class my_products : AppCompatActivity() {
    private var my_prod_recycler: RecyclerView? = null
    private var my_prod_griditem: ArrayList<my_product_grid>? = null
    private var my_product_adapter: my_product_adapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_products)
        my_prod_recycler = findViewById(R.id.my_prod_recycler)
        my_prod_griditem = ArrayList<my_product_grid>()
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("user_product")

        Log.d("buy2", dbRef.toString())

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (ds: DataSnapshot in p0.children) {
                    for (ds1: DataSnapshot in ds.children) {
                        try {
                            var l = my_product_grid()
                            l.Product_description = ds1.child("product_description").value.toString()
                            l.Product_name = ds1.child("product_name").value.toString()
                            l.Product_Price = ds1.child("Product_Price").value.toString()
                            l.url = ds1.child("url").value.toString()
                            l.Product_id = ds1.child("product_id").value.toString()
                            l.category = ds1.child("category").value.toString()
                            my_prod_griditem!!.add(l)
                            Log.d("buy", ds1.child("product_description").value.toString())
                            Log.d("buy3", ds1.child("product_name").value.toString())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                my_product_adapter!!.update(my_prod_griditem!!)
            }
        })
        my_prod_recycler?.layoutManager = LinearLayoutManager(this)
        my_prod_recycler?.setHasFixedSize(true)
        my_product_adapter= my_product_adapter(this, my_prod_griditem!!)
        my_prod_recycler?.adapter = my_product_adapter
        val fabbtn = findViewById(R.id.fab) as FloatingActionButton
        fabbtn.setOnClickListener {
            val intent = Intent(this, categories_activity::class.java)
            startActivity(intent)
        }
    }
}
