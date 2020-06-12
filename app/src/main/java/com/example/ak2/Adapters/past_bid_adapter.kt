package com.example.ak2.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ak2.Models.current_bid_grid
import com.example.ak2.Models.past_bid_grid
import com.example.ak2.R
import com.example.ak2.auction_item_info

class past_bid_adapter (var context: Context?, var arrayList: ArrayList<past_bid_grid>) :
    RecyclerView.Adapter<past_bid_adapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.past_bid_layout, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun update(list: ArrayList<past_bid_grid>) {
        this.arrayList = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: past_bid_adapter.ItemHolder, position: Int) {
        val GridItem: past_bid_grid= arrayList.get(position)
        holder.itname.text = GridItem.item_name
        holder.bdamt.text = GridItem.bid_amount
        holder.cnt.text = GridItem.current_date_time
        holder.card.setOnClickListener {
            val intent = Intent(context, auction_item_info::class.java)
            intent.putExtra("auct_id", GridItem.auct_id)
            intent.putExtra("itemname", GridItem.item_name)
            intent.putExtra("item_amt", GridItem.item_amt)
            it.context.startActivity(intent)
        }
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itname = itemView.findViewById<TextView>(R.id.past_item_name)
        var bdamt = itemView.findViewById<TextView>(R.id.past_bideramount)
        var cnt = itemView.findViewById<TextView>(R.id.past_bid_placed_on_time)
        var card = itemView.findViewById<CardView>(R.id.past_card)
    }
}