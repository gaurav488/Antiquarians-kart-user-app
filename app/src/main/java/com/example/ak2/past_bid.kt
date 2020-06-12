package com.example.ak2


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ak2.Adapters.current_bid_adapter
import com.example.ak2.Adapters.past_bid_adapter
import com.example.ak2.Models.current_bid_grid
import com.example.ak2.Models.past_bid_grid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class past_bid : Fragment() {
    private var past_recyclerView: RecyclerView? = null
    private var past_griditem: ArrayList<past_bid_grid>? = null
    private var past_adapter: past_bid_adapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view= inflater.inflate(R.layout.fragment_past_bid, container, false)
        past_recyclerView = view.findViewById(R.id.past_bid_recycler)
        past_griditem = ArrayList<past_bid_grid>()
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val  dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("past_bid")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                past_griditem!!.clear()
                if(p0.exists()) {
                    for (ds: DataSnapshot in p0.children) {
                        try {
                            var h = past_bid_grid()
                            h.item_name = ds.child("item_name").value.toString()
                            h.bid_amount = ds.child("bid_amount").value.toString()
                            h.auct_id = ds.child("auct_id").value.toString()
                            h.current_date_time = ds.child("current_date_time").value.toString()
                            h.item_amt = ds.child("item_amt").value.toString()
                            val d: String = ds.child("auct_end_date").value.toString()
                            val t: String = ds.child("auct_end_time").value.toString()
                            val calen = Calendar.getInstance()
                            val currentdate = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                            val savecurrent = currentdate.format(calen.time)
                            val currenttime = SimpleDateFormat("hh:mm a", Locale.getDefault())
                            val savecurrenttime = currenttime.format(calen.time)
                            var formatter: SimpleDateFormat = SimpleDateFormat("dd MMM, yyyy")
                            var timeInMs = formatter.parse(d).time
                            var currentconvert = formatter.parse(savecurrent).time
                            if(currentconvert.compareTo(timeInMs)<=0 && savecurrenttime.compareTo(t)<0) {
                            past_griditem!!.add(h)
                            }
                            Log.d("data", ds.child("item_description").value.toString())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        past_adapter!!.update(past_griditem!!)
                    }
                }
            }
        })
        past_recyclerView?.layoutManager = LinearLayoutManager( context)
       past_adapter = past_bid_adapter(context, past_griditem!!)
       past_recyclerView?.adapter = past_adapter
        return view
    }

}
