package com.example.ak2.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ak2.Models.current_bid_grid
import com.example.ak2.Models.saved_item_grid
import com.example.ak2.R
import com.example.ak2.auction_item_info
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class current_bid_adapter (var context: Context?, var arrayList: ArrayList<current_bid_grid>) :
    RecyclerView.Adapter<current_bid_adapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.current_bid_layout, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun update(list: ArrayList<current_bid_grid >) {
        this.arrayList = list
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: current_bid_adapter.ItemHolder, position: Int) {
        val GridItem: current_bid_grid  = arrayList.get(position)
        holder.itname.text = GridItem.item_name.toString()
        holder.bdamt.text = GridItem.bid_amount
        holder.cnt.text=GridItem.current_date_time
        holder.card.setOnClickListener {
            val intent = Intent(context, auction_item_info::class.java)
            intent.putExtra("auct_id", GridItem.auct_id.toString())
            Log.d("disp22",GridItem.auct_id.toString())
            intent.putExtra("itemname",GridItem.item_name.toString())
            Log.d("disp223",GridItem.item_name.toString())
            intent.putExtra("item_amt",GridItem.item_amt.toString())
            it.context.startActivity(intent)
        }
   }
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itname = itemView.findViewById<TextView>(R.id.current_item_name)
        var bdamt = itemView.findViewById<TextView>(R.id.current_bideramount)
        var cnt = itemView.findViewById<TextView>(R.id.bid_placed_on_time)
        var card = itemView.findViewById<CardView>(R.id.current_card)
    }
}