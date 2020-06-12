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
import com.bumptech.glide.Glide
import com.example.ak2.Models.Buy_now_grid
import com.example.ak2.R
import com.example.ak2.new_product_info
import android.graphics.Rect;
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView.*
import com.example.ak2.Auction_details

class Buy_now_adapter (var context: Context?, var arrayList: ArrayList<Buy_now_grid>) :
    Adapter<Buy_now_adapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.buy_now_grid, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
    fun update(list:ArrayList<Buy_now_grid>){
        this.arrayList=list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: Buy_now_adapter.ItemHolder, position: Int) {
        val GridItem: Buy_now_grid = arrayList.get(position)
        Glide.with(context!!).load(GridItem.url).placeholder(R.drawable.hammerauct).into(holder.prod_images)
        holder.product_name.text ="Name :"+GridItem.product_name
        holder.Product_price.text="Price :"+GridItem.Product_Price
       // holder.product_description.text="Description :"+GridItem.product_description
        holder.card.setOnClickListener{
            val intent= Intent(context,new_product_info::class.java)
            intent.putExtra("pid",GridItem.product_id.toString())
            intent.putExtra("category",GridItem.category.toString())
            intent.putExtra("proprice",GridItem.Product_Price)
            intent.putExtra("Uid",GridItem.Uid)
            it.context.startActivity(intent)
        }
    }
    class ItemHolder(itemView: View) : ViewHolder(itemView) {
        var prod_images = itemView.findViewById<ImageView>(R.id.prodimg)
        var product_name= itemView.findViewById<TextView>(R.id.product_name2)
        var Product_price = itemView.findViewById<TextView>(R.id.product_amt)
      //  var product_description=itemView.findViewById<TextView>(R.id.product_desc)
        var card = itemView.findViewById<CardView>(R.id.card)
    }
}
class SpacesItemDecoration(private val mSpace: Int) : ItemDecoration() {
    fun RecyclerView.getItemOffsets(
        outRect: Rect,
        view: View?,
        state: State?
    ) {
        outRect.left = mSpace
        outRect.right = mSpace
        outRect.bottom = mSpace
        if (getChildAdapterPosition(view!!) == 0) outRect.top =
            mSpace
    }

}
