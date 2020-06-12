package com.example.ak2.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ak2.Models.cart_grid
import com.example.ak2.R
import com.example.ak2.new_product_info
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class cart_adapter (var context: Context?, var arrayList: ArrayList<cart_grid>) :
    RecyclerView.Adapter<cart_adapter.ItemHolder>() {
    private var total:Int=0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_grid_layout, parent, false)
        return ItemHolder(viewHolder)
    }
    override fun getItemCount(): Int {
        return arrayList.size
    }
    fun update(list:ArrayList<cart_grid>){
        this.arrayList=list
        notifyDataSetChanged()
    }
    fun ToatalPrice(): Int {
        var count=0
        for (i in arrayList)
        {
            count=count + i.Product_Price.toInt()

        }
        Log.d("total", count.toString())
        return count
    }
    override fun onBindViewHolder(holder: cart_adapter.ItemHolder, position: Int) {
        val GridItem: cart_grid = arrayList.get(position)
        val  dbRef = FirebaseDatabase.getInstance().getReference().child("Cart").child("Users")

        Glide.with(context!!).load(GridItem.url).dontAnimate().into(holder.cart_prod_images)
        holder.cart_product_name.text = "Name : "+GridItem.Product_name
        holder.cart_Product_price.text="Price : "+GridItem.Product_Price+"$"
        holder.cart_product_description.text="Description : "+GridItem.Product_description
        holder.cart_card.setOnClickListener{
            val intent= Intent(context, new_product_info::class.java)
            intent.putExtra("pid",GridItem.Product_id.toString())
            intent.putExtra("category",GridItem.category.toString())
            it.context.startActivity(intent)
        }

       holder.remove.setOnClickListener {
           Log.d("prod",GridItem.Product_id.toString())
           val uid= FirebaseAuth.getInstance().currentUser?.uid.toString()
           Log.d("uid",uid)
           AlertDialog.Builder(context).apply {
               setTitle("Are you Sure you want to remove from the cart?")
               setPositiveButton("OK"){ _,_ ->
                       dbRef.child(uid)
                           .child(GridItem.Product_id).removeValue()
                           .addOnSuccessListener {
                            Toast.makeText(context,"Item removed from cart successfully",Toast.LENGTH_SHORT).show()
                               arrayList.removeAt(position)
                               notifyItemRemoved(position)
                               notifyItemRangeChanged(position,arrayList.size)
                               update(arrayList)
                           }
                  }
               setNegativeButton("Cancel"){ _, _ ->
               }
           }.create().show()
       }
    }
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cart_prod_images = itemView.findViewById<ImageView>(R.id.cart_icon_img)
        var cart_product_name= itemView.findViewById<TextView>(R.id.cart_prod_name)
        var cart_Product_price = itemView.findViewById<TextView>(R.id.cart_prod_price)
        var cart_product_description=itemView.findViewById<TextView>(R.id.cart_prod_des)
        var cart_card = itemView.findViewById<CardView>(R.id.cart_card)
        var remove = itemView.findViewById<ImageView>(R.id.remove_button)
    }
}