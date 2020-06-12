package com.example.ak2


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.icu.text.Transliterator
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ViewFlipper
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.ak2.Adapters.Home_adapter
import com.example.ak2.Adapters.SpacesItem
import com.example.ak2.Adapters.viewpager_adapter
import com.example.ak2.Models.auc_grid
import com.example.ak2.Models.home_grid
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Context
import kotlinx.android.synthetic.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

class HomeFragment : Fragment(){
    private var recyclerView: RecyclerView? = null
    private var home_griditem: ArrayList<home_grid>? = null
    private var home_adapter: Home_adapter? = null
    lateinit var mpager:ViewPager
    lateinit var dots:Array<ImageView>
    lateinit var dotsLayout : LinearLayout
    lateinit var adapter2:viewpager_adapter
    var currentpage:Int=0
    val DELAY_MS:Long=5000
    val PERIOD_MS:Long=5000
     lateinit var timer:Timer
    var path = intArrayOf(R.drawable.chair,R.drawable.clock,R.drawable.vase)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        adapter2=viewpager_adapter(context!!,path)
        mpager.adapter= adapter2
        dotsLayout = view.findViewById(R.id.dots_layout) as LinearLayout
        createdots(0)
        updatepage()
        mpager.addOnPageChangeListener(object:ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
                currentpage = position
                try {
                    createdots(position)
                }catch (e: NullPointerException){

                }
            }
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view2 =inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view2.findViewById(R.id.recyclev)
        home_griditem = ArrayList<home_grid>()

        val  home_dbRef = FirebaseDatabase.getInstance().getReference().child("auction")
        home_dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
               home_griditem!!.clear()
                for (ds: DataSnapshot in p0.children)
                {
                        try {
                            var h = home_grid()
                            h.item_name = ds.child("item_name").value.toString()
                            h.item_amt = ds.child("item_amt").value.toString()
                            h.url = ds.child("url").value.toString()
                            h.auct_id = ds.child("auct_id").value.toString()
                            h.count = ds.child("count").value.toString()
                            home_griditem!!.add(h)
                            home_griditem!!.sortBy { it.count }
                            home_griditem!!.reverse()
                            Log.d("data", ds.child("item_description").value.toString())

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                }
                home_adapter!!.update(home_griditem!!)
            }
        })
        recyclerView?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val decoration = SpacesItem(16)
        mpager= view2.findViewById(R.id.viewerpager) as ViewPager
        recyclerView?.addItemDecoration(decoration)
        home_adapter = Home_adapter(context, home_griditem!!)
        recyclerView?.adapter = home_adapter
        return view2
    }

   fun createdots(position:Int){
        if(dotsLayout!=null){
            dotsLayout.removeAllViews()
        }
        dots = Array(path.size) { ImageView(context)}
       for (i in 0..path.size-1){
            dots[i] = ImageView(context)
            if(i == position){
                dots[i].setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.activedots))
            }
            else{
                dots[i].setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.inactivedots))
            }
            var params:LinearLayout.LayoutParams= LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(4,0,4,0)
            dotsLayout.addView(dots[i],params)
        }
    }
    fun updatepage(){
        var handler =Handler()
        val update:Runnable= Runnable {
            if(currentpage == path.size){
                currentpage=0
            }
            mpager.setCurrentItem(currentpage++,true)
        }
        timer = Timer()
        timer.schedule(object :TimerTask(){
            override fun run(){
                handler.post(update)
        }
        },DELAY_MS,PERIOD_MS)
    }
   

}