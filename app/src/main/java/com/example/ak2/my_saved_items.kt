package com.example.ak2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.ak2.Adapters.Home_adapter
import com.example.ak2.Adapters.SpacesItem
import com.example.ak2.Adapters.saved_item_adapter
import com.example.ak2.Models.home_grid
import com.example.ak2.Models.saved_item_grid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class my_saved_items : AppCompatActivity() {
    private var saved_recyclerView: RecyclerView? = null
    private var saved_griditem: ArrayList<saved_item_grid>? = null
    private var saved_adapter: saved_item_adapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_saved_items)
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        saved_recyclerView = findViewById(R.id.id_saved_item_recycler)
        saved_griditem = ArrayList<saved_item_grid>()

        val  home_dbRef = FirebaseDatabase.getInstance().getReference().child("saved_items").child(uid)
        home_dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                saved_griditem!!.clear()
                for (ds: DataSnapshot in p0.children)
                {
                    try {
                        var h = saved_item_grid()
                        h.item_name = ds.child("item_name").value.toString()
                        h.item_amt = ds.child("item_amt").value.toString()
                        h.url = ds.child("url").value.toString()
                        h.auct_id = ds.child("auct_id").value.toString()
                        h.count = ds.child("count").value.toString()
                        val d: String = ds.child("auct_end_date").value.toString()
                        val t: String = ds.child("auct_end_time").value.toString()
                        val calen = Calendar.getInstance()
                        val currentdate = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                        val savecurrent = currentdate.format(calen.time)
                        val currenttime = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        val savecurrenttime = currenttime.format(calen.time)
                        val auctid= ds.child("auct_id").value.toString()
                        var formatter: SimpleDateFormat = SimpleDateFormat("dd MMM, yyyy")
                        var timeInMs=formatter.parse(d).time
                        var currentconvert=formatter.parse(savecurrent).time

                        if(currentconvert.compareTo(timeInMs)>=0 && savecurrenttime.compareTo(t)>0){
                            val remove = FirebaseDatabase.getInstance().getReference().child("saved_items").child(uid)
                            remove.child(auctid).removeValue()
                        }else{
                        saved_griditem!!.add(h)
                       saved_griditem!!.reverse()
                        }
                        Log.d("data", ds.child("item_description").value.toString())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                saved_adapter!!.update(saved_griditem!!)
            }
        })
        saved_recyclerView?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val decoration = SpacesItem(16)
        saved_recyclerView?.addItemDecoration(decoration)
       saved_adapter = saved_item_adapter(applicationContext, saved_griditem!!)
        saved_recyclerView?.adapter = saved_adapter
    }
}
