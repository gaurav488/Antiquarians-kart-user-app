package com.example.ak2.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ak2.Models.Buy_now_grid
import com.example.ak2.Models.ordered_grid
import com.example.ak2.R
import com.example.ak2.new_product_info

class ordered_prod_adapater (var context: Context?, var arrayList: ArrayList<ordered_grid>) :
    RecyclerView.Adapter<ordered_prod_adapater.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.ordered_prod_item_layout, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
    fun update(list:ArrayList<ordered_grid>){
        this.arrayList=list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ordered_prod_adapater.ItemHolder, position: Int) {
        val GridItem: ordered_grid = arrayList.get(position)
        Glide.with(context!!).load(GridItem.url).placeholder(R.drawable.hammerauct).into(holder.ordered_prod_image_icon)
        holder.ordered_prod_name.text ="Name :"+GridItem.product_name
        holder.ordered_prod_price.text="Price :"+GridItem.Product_Price
        holder.ordered_user_address.text="Address :"+GridItem.orderers_address+","+GridItem.orderers_cityname
        holder.ordered_user_name.text="Name :"+GridItem.orderers_name
        holder.ordered_prod_phone_number.text="Phone No"+GridItem.orderers_phone

    }
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ordered_prod_image_icon = itemView.findViewById<ImageView>(R.id.ordered_prod_imageicon)
        var ordered_prod_name= itemView.findViewById<TextView>(R.id.ordered_prod_name)
        var ordered_prod_price= itemView.findViewById<TextView>(R.id.ordered_prod_price)
        var ordered_user_address=itemView.findViewById<TextView>(R.id.ordered_prod_address_cityname)
        var ordered_user_name=itemView.findViewById<TextView>(R.id.ordered_prod_user_name)
        var ordered_prod_phone_number=itemView.findViewById<TextView>(R.id.ordered_prod_phone_number)
    }
}
