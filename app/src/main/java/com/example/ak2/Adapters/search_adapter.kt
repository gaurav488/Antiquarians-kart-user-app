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
import com.example.ak2.Models.search_grid
import com.example.ak2.R
import com.example.ak2.new_product_info
import com.example.ak2.search_activity

class search_adapter (var context: Context?, var arrayList: ArrayList<search_grid>) :
    RecyclerView.Adapter<search_adapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_grid_layout, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
    fun update(list:ArrayList<search_grid>){
        this.arrayList=list
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: search_adapter.ItemHolder, position: Int) {
        val GridItem: search_grid = arrayList.get(position)
        Glide.with(context!!).load(GridItem.url).dontAnimate().into(holder.prod_images)
        holder.product_name.text = GridItem.product_name
        holder.Product_price.text=GridItem.Product_Price
        holder.product_description.text=GridItem.product_description
        holder.card.setOnClickListener{
            val id= GridItem.product_id.toString()
            val intent= Intent(context, new_product_info::class.java)
            intent.putExtra("pid",id)
            intent.putExtra("category",GridItem.category.toString())
            it.context.startActivity(intent)
        }
    }
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var prod_images = itemView.findViewById<ImageView>(R.id.search_prodimg)
        var product_name= itemView.findViewById<TextView>(R.id.search_product_name2)
        var Product_price = itemView.findViewById<TextView>(R.id.search_product_amt)
        var product_description=itemView.findViewById<TextView>(R.id.search_product_desc)
        var card = itemView.findViewById<CardView>(R.id.search_card)
    }
    class SpacesItem(private val m: Int) : RecyclerView.ItemDecoration() {
        fun RecyclerView.getItemOffsets(
            outRect: Rect,
            view: View?,
            state: RecyclerView.State?
        ) {
            outRect.left = m
            outRect.right = m
            outRect.bottom = m
            if (getChildAdapterPosition(view!!) == 0) outRect.top = m
        }

    }
}
