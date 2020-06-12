package com.example.ak2.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ak2.*
import com.example.ak2.Models.account_grid

class account_adapter (var context: Context?, var arrayList: ArrayList<account_grid>) :
    RecyclerView.Adapter<account_adapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.acccount_item_layout, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: account_adapter.ItemHolder, position: Int) {
        val GridItem: account_grid = arrayList.get(position)

        holder.left_icon.setImageResource(GridItem.left_icon!!)
        holder.right_icon.setImageResource(GridItem.right_icon!!)
        holder.titles.text = GridItem.title

        holder.card.setOnClickListener {
            if(position == 0){
            val intent=Intent(context,VIewpage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }
            if(position == 1){
                val intent=Intent(context, AuctionTab::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }
            if(position == 2){
                val intent=Intent(context,my_products::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }
            if(position == 3){
                val intent=Intent(context,cart_activity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }
            if(position == 4){
                val intent=Intent(context,my_saved_items::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }
            if(position == 5){
                val intent=Intent(context,ordered_products::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }
        }
    }
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var left_icon = itemView.findViewById<ImageView>(R.id.left_acc_icon)
        var titles = itemView.findViewById<TextView>(R.id.setting_acc_text)
        var right_icon = itemView.findViewById<ImageView>(R.id.right_acc_icon)
        var card = itemView.findViewById<CardView>(R.id.account_card)
    }

}
