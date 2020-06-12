package com.example.ak2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ak2.Adapters.Buy_now_adapter
import com.example.ak2.Adapters.SpacesItemDecoration
import com.example.ak2.Adapters.search_adapter
import com.example.ak2.Models.Buy_now_grid
import com.example.ak2.Models.search_grid
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class search_activity : AppCompatActivity() {
    private var search_recycler: RecyclerView? = null
    private var search_griditem: ArrayList<search_grid>? = null
    private var search_adapter: search_adapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_activity)

        val search_category= getIntent().getExtras()?.get("category").toString()
        search_recycler = findViewById(R.id.search_recy)
        search_griditem = ArrayList<search_grid>()

        val  dbRef = FirebaseDatabase.getInstance().getReference().child("new_product").child(search_category)

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (ds: DataSnapshot in p0.children)
                {
                        try {
                            var s= search_grid()
                            s.product_description=ds.child("product_description").value.toString()
                            s.product_name=ds.child("product_name").value.toString()
                            s.Product_Price=ds.child("Product_Price").value.toString()
                            s.url=ds.child("url").value.toString()
                            s.product_id=ds.child("product_id").value.toString()
                            s.category=ds.child("category").value.toString()
                            search_griditem!!.add(s)
                            Log.d("buy",ds.child("product_description").value.toString())
                        }catch (e: Exception)
                        {
                            e.printStackTrace()
                        }

                }
                search_adapter!!.update(search_griditem!!)
            }
        })
        search_recycler?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val decoration = SpacesItemDecoration(16)
        search_recycler?.addItemDecoration(decoration)
        search_adapter = search_adapter(applicationContext, search_griditem!!)
        search_recycler?.adapter = search_adapter
    }
}


