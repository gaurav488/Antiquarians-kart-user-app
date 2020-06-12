package com.example.ak2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ak2.Adapters.Buy_now_adapter
import com.example.ak2.Adapters.SpacesItemDecoration
import com.example.ak2.Models.Buy_now_grid
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class buynow : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var buy_griditem: ArrayList<Buy_now_grid>? = null
    private var buy_adapter: Buy_now_adapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_buynow, container, false)
        (activity as AppCompatActivity).supportActionBar?.setTitle("Buy Now")
        recyclerView = view.findViewById(R.id.recy)
       buy_griditem = ArrayList<Buy_now_grid>()
        val  dbRef = FirebaseDatabase.getInstance().getReference().child("new_product")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (ds: DataSnapshot in p0.children)
                {
                    for (ds1:DataSnapshot in ds.children)
                    {
                        try {
                            var l= Buy_now_grid()
                            l.product_description=ds1.child("product_description").value.toString()
                            l.product_name=ds1.child("product_name").value.toString()
                            l.Product_Price=ds1.child("Product_Price").value.toString()
                            l.url=ds1.child("url").value.toString()
                            l.product_id=ds1.child("product_id").value.toString()
                            l.category=ds1.child("category").value.toString()
                            l.Uid=ds1.child("Uid").value.toString()
                            buy_griditem!!.add(l)
                            Log.d("buy",ds1.child("product_description").value.toString())
                        }catch (e: Exception)
                        {
                            e.printStackTrace()
                        }
                    }
                }
                buy_adapter!!.update(buy_griditem!!)
            }
        })

        recyclerView?.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        val decoration = SpacesItemDecoration(16)
        recyclerView?.addItemDecoration(decoration)
        buy_adapter = Buy_now_adapter(context, buy_griditem!!)
        recyclerView?.adapter = buy_adapter
        return view
    }
}
