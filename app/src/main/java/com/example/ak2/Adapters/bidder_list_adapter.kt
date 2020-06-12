package com.example.ak2.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ak2.Models.bidder_list_grid
import com.example.ak2.R
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class bidder_list_adapter (var context: Context?, var arrayList: ArrayList<bidder_list_grid>) :
    RecyclerView.Adapter<bidder_list_adapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.bidder_list_layout, parent, false)
        return ItemHolder(viewHolder)
    }
    override fun getItemCount(): Int {
        return arrayList.size
    }
    fun update(list:ArrayList<bidder_list_grid>){
        this.arrayList=list
        Collections.sort(arrayList,
            Comparator { o1, o2 ->
                o1.bid_amount.compareTo(o2.bid_amount)
            })
        arrayList.reverse()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder:bidder_list_adapter.ItemHolder, position: Int) {
        val GridItem: bidder_list_grid= arrayList.get(position)
        holder.bidername.text = GridItem.username
        holder.bideramount.text=GridItem.bid_amount
        holder.time.text=GridItem.current_date_time
    }
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bideramount = itemView.findViewById<TextView>(R.id.bideramount)
        var bidername= itemView.findViewById<TextView>(R.id.bidername)
        var time = itemView.findViewById<TextView>(R.id.time)
    }
}