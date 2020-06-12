package com.example.ak2


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ak2.Adapters.Buy_now_adapter
import com.example.ak2.Adapters.SpacesItemDecoration
import com.example.ak2.Adapters.current_bid_adapter
import com.example.ak2.Adapters.saved_item_adapter
import com.example.ak2.Models.Buy_now_grid
import com.example.ak2.Models.current_bid_grid
import com.example.ak2.Models.home_grid
import com.example.ak2.Models.saved_item_grid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class current_bid : Fragment() {
    private var current_recyclerView: RecyclerView? = null
    private var current_griditem: ArrayList<current_bid_grid>? = null
    private var current_adapter: current_bid_adapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view=inflater.inflate(R.layout.fragment_current_bid, container, false)
        current_recyclerView = view.findViewById(R.id.current_bid_recycler)
        current_griditem = ArrayList<current_bid_grid>()
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val  dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("active_bid")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (ds: DataSnapshot in p0.children) {
                    try {
                        var h = current_bid_grid()
                        h.item_name = ds.child("item_name").value.toString()
                        h.bid_amount= ds.child ("bid_amount").value.toString()
                        h.auct_id = ds.child("auct_id").value.toString()
                        h.current_date_time = ds.child("current_date_time").value.toString()
                        h.item_amt= ds.child("item_amt").value.toString()
                        current_griditem!!.add(h)
                        Log.d("data", ds.child("item_description").value.toString())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    current_adapter!!.update(current_griditem!!)
                }
            }
        })
        current_recyclerView?.layoutManager = LinearLayoutManager( context)
        current_adapter = current_bid_adapter(context, current_griditem!!)
        current_recyclerView?.adapter = current_adapter
        return view
    }
}
