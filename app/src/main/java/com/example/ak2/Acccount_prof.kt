package com.example.ak2


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ak2.Adapters.account_adapter
import com.example.ak2.Models.account_grid



class Acccount_prof : Fragment() {
    private var account_recycler: RecyclerView? = null
    private var account_griditem: ArrayList<account_grid>? = null
    private var account_adapter: account_adapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_acccount_prof,container,false)
        (activity as AppCompatActivity).supportActionBar?.setTitle("My Account")
        account_recycler = view.findViewById(R.id.acount_recycler)
        account_griditem = ArrayList<account_grid>()
        account_recycler?.layoutManager = LinearLayoutManager(context)
        account_recycler?.setHasFixedSize(true)
        account_griditem = ArrayList()
        account_griditem = items()
        account_adapter = account_adapter(context, account_griditem!!)
        account_recycler?.adapter = account_adapter
        return view

    }
    private fun items(): ArrayList<account_grid> {
        var arrayList: ArrayList<account_grid> = ArrayList()
        arrayList.add(account_grid("Profile", R.drawable.ic_action_setting,R.drawable.right_angle_setting))
        arrayList.add(account_grid("My Auction Bid", R.drawable.auction_hammer,R.drawable.right_angle_setting))
        arrayList.add(account_grid("Sell Your Item", R.drawable.ic_add_shopping_cart_black_24dp,R.drawable.right_angle_setting))
        arrayList.add(account_grid("My Cart", R.drawable.abcshop,R.drawable.right_angle_setting))
        arrayList.add(account_grid("My Saved items", R.drawable.ic_action_favorites,R.drawable.right_angle_setting))
        arrayList.add(account_grid("My Oredered Items", R.drawable.order,R.drawable.right_angle_setting))


        return arrayList
    }
    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context: Context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_animation_fall_down)
        recyclerView.layoutAnimation = controller
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }
}











































/*  override fun onActivityCreated(savedInstanceState: Bundle?) {
      super.onActivityCreated(savedInstanceState)
      setting_card.setOnClickListener() {
          val intent = Intent(this@Acccount_prof.context, VIewpage::class.java)
          startActivity(intent)
      }
          auction_card2.setOnClickListener(){
          val intent = Intent (this@Acccount_prof.context, AuctionTab::class.java)
              startActivity(intent)
      }
      sell_card2.setOnClickListener{
          startActivity(Intent(context,categories_activity::class.java))
      }
      my_cart_card.setOnClickListener {
          startActivity(Intent(context,cart_activity::class.java))
      }

  }
*/



