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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.ak2.Models.Buy_now_grid
import com.example.ak2.Models.home_grid
import com.example.ak2.R
import com.example.ak2.auction_item_info

class Home_adapter (var context: Context?, var arrayList: ArrayList<home_grid>) :
    RecyclerView.Adapter<Home_adapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_layout, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
    fun update(list:ArrayList<home_grid>){
        this.arrayList=list
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: Home_adapter.ItemHolder, position: Int){
        val GridItem: home_grid = arrayList.get(position)
            Glide.with(context!!).load(GridItem.url).placeholder(R.drawable.hammerauct).into(holder.icons)
            holder.titles.text = GridItem.item_name
            holder.price.text = GridItem.item_amt
            holder.card.setOnClickListener {
                val intent = Intent(context, auction_item_info::class.java)
                intent.putExtra("auct_id", GridItem.auct_id)
                intent.putExtra("item_amt", GridItem.item_amt)
                intent.putExtra("itemname",GridItem.item_name)
                it.context.startActivity(intent)
            }
    }
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icons = itemView.findViewById<ImageView>(R.id.home_icon_image_view)
        var titles = itemView.findViewById<TextView>(R.id.name_home_view)
        var price = itemView.findViewById<TextView>(R.id.price_home_view)
        var card = itemView.findViewById<CardView>(R.id.card)
    }
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