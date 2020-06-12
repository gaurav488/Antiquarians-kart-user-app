package com.example.ak2


import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ak2.Adapters.auc_gridadapter
import com.example.ak2.Models.auc_grid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AuctionFragment : Fragment() {
    private var recyclView: RecyclerView? = null
    lateinit var auc_griditem: ArrayList<auc_grid>
    lateinit var  garbage:ArrayList<auc_grid>
    private var linear: LinearLayoutManager? = null
    lateinit var adapter: auc_gridadapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_auction, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()
        recyclView = view.findViewById(R.id.auc_recy)
        auc_griditem = ArrayList<auc_grid>()
        garbage=ArrayList<auc_grid>()
        val sear = view.findViewById<SearchView>(R.id.search_card)
        linear = LinearLayoutManager(context)
        recyclView?.layoutManager = linear
        recyclView?.setHasFixedSize(true)
        adapter = auc_gridadapter(context, auc_griditem!!)
        recyclView?.adapter = adapter
        val dbRef = FirebaseDatabase.getInstance().getReference().child("auction")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {
                auc_griditem!!.clear()
                if (p0.exists()) {
                    for (ds: DataSnapshot in p0.children) {
                        try {
                            var m = auc_grid()
                            m.item_description = ds.child("item_description").value.toString()
                            m.item_name = ds.child("item_name").value.toString()
                            m.item_amt = ds.child("item_amt").value.toString()
                            m.auct_end_date = ds.child("auct_end_date").value.toString()
                            m.auct_end_time = ds.child("auct_end_time").value.toString()
                            m.url = ds.child("url").value.toString()
                            m.auct_id = ds.child("auct_id").value.toString()
                            val d: String = ds.child("auct_end_date").value.toString()
                            val t: String = ds.child("auct_end_time").value.toString()
                            val calen = Calendar.getInstance()
                            val currentdate = SimpleDateFormat("dd MMM, yyyy",Locale.getDefault())
                            val savecurrent = currentdate.format(calen.time)
                            val currenttime = SimpleDateFormat("hh:mm a",Locale.getDefault())
                            val savecurrenttime = currenttime.format(calen.time)
                            var formatter:SimpleDateFormat= SimpleDateFormat("dd MMM, yyyy")
                            var timeInMs=formatter.parse(d).time
                            var currentconvert=formatter.parse(savecurrent).time
                            Log.d("c_millisec",currentconvert.toString())
                            Log.d("millisec",timeInMs.toString())
                               if(currentconvert.compareTo(timeInMs)>=0 && savecurrenttime.compareTo(t)>0) {
                                val hashmap: java.util.HashMap<String, String> = java.util.HashMap<String, String>()
                                val auct: String = ds.child("auct_id").value.toString()
                                val itname: String = ds.child("item_name").value.toString()
                                val itdes: String = ds.child("item_description").value.toString()
                                val itamt: String = ds.child("item_amt").value.toString()
                                    val estimated_end_bid: String = ds.child("estimated_end_bid").value.toString()
                                    val url: String = ds.child("url").value.toString()
                                    hashmap.put("auct_id",auct)
                                    hashmap.put("item_name",itname)
                                    hashmap.put("item_description",itdes)
                                    hashmap.put("item_amt",itamt)
                                    hashmap.put("url",url)
                                    hashmap.put("estimated_end_bid",estimated_end_bid)
                                    hashmap.put("auct_end_date",d)
                                    hashmap.put("auct_end_time",t)
                                val ref = FirebaseDatabase.getInstance().getReference().child("history")
                                ref.child(auct).setValue(hashmap)
                                val remove = FirebaseDatabase.getInstance().getReference().child("auction")
                                remove.child(auct).removeValue().addOnCanceledListener {
                                    Toast.makeText(context,"item removed success",Toast.LENGTH_SHORT).show()
                                }
                                   val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                                   val firedb= FirebaseDatabase.getInstance().getReference("Users").child(uid)
                                   firedb.child("active_bid").child(auct).removeValue()
                             } else {
                                auc_griditem.add(m)
                            }
                            Log.d("data", ds.child("item_description").value.toString())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    adapter!!.update(auc_griditem!!)
                }
            }
        })
        sear.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search_fun(newText)
                return true
            }
        })
        return view
    }
    private fun search_fun(newText: String?) {

        val griditem = ArrayList<auc_grid>()
        for (obj: auc_grid in auc_griditem) {
            if (obj.item_name.toLowerCase().contains(newText!!.toLowerCase())) {
                griditem.add(obj)
            }
        }
        adapter?.update(griditem!!)
        linear = LinearLayoutManager(context)
        recyclView?.layoutManager = linear
        recyclView?.setHasFixedSize(true)
        adapter = auc_gridadapter(context, griditem!!)
        recyclView?.adapter = adapter
    }
    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}

/*   var edate:Date?=formatter.parse(d)
                         var cdate:Date?=formatter.parse(savecurrent)
                        */

/* var cdt=savecurrent+" "+savecurrenttime
 var current_timeInMs=formatter.parse(cdt).time
 Log.d("c_millisec",current_timeInMs.toString())

 var diff=current_timeInMs-timeInMs
 Log.d("diff",diff.toString())*/
/*
  if(current_timeInMs-timeInMs>0)
      auc_griditem.add(m)*/