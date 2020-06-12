package com.example.ak2.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ak2.Models.cart_grid
import com.example.ak2.Models.my_product_grid
import com.example.ak2.R
import com.example.ak2.edit_my_product_info
import com.example.ak2.new_product_info
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class my_product_adapter (var context: Context?, var arrayList: ArrayList<my_product_grid>) :
    RecyclerView.Adapter<my_product_adapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_prod_item_layout, parent, false)
        return ItemHolder(viewHolder)
    }
    override fun getItemCount(): Int {
        return arrayList.size
    }
    fun update(list:ArrayList<my_product_grid>){
        this.arrayList=list
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: my_product_adapter.ItemHolder, position: Int) {
        val GridItem: my_product_grid = arrayList.get(position)
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val  dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
        Glide.with(context!!).load(GridItem.url).dontAnimate().into(holder.my_prod_images)
        holder.my_product_name.text = "Name : "+GridItem.Product_name
        holder.my_Product_price.text="Price : "+GridItem.Product_Price+"$"
        holder.my_product_description.text="Description : "+GridItem.Product_description
        holder.my_card.setOnClickListener{
            val intent= Intent(context, new_product_info::class.java)
            intent.putExtra("pid",GridItem.Product_id.toString())
            intent.putExtra("category",GridItem.category.toString())
            it.context.startActivity(intent)
        }

        holder.options.setOnClickListener {
            val dbRef = FirebaseDatabase.getInstance().getReference().child("new_product").child(GridItem.category.toString())
            AlertDialog.Builder(context).apply {
                setTitle("Do you want to Edit or Remove?")
                setPositiveButton("Remove") { _, _ ->
                    dbRef.child(GridItem.Product_id).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(context, "Auction has been removed", Toast.LENGTH_SHORT)
                                .show()
                            arrayList.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, arrayList.size)
                            update(arrayList)
                        }
                    val userref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("user_product").child(GridItem.category)
                    userref.child(GridItem.Product_id).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(context, "Auction has been removed", Toast.LENGTH_SHORT)
                                .show()
                            arrayList.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, arrayList.size)
                            update(arrayList)
                        }

                }
                setNeutralButton("Edit") { _, _ ->
                    val intent= Intent(context,edit_my_product_info::class.java)
                    intent.putExtra("category",GridItem.category.toString())
                    intent.putExtra("prod_id",GridItem.Product_id.toString())
                    context!!.startActivity(intent)
                }
                setNegativeButton("Cancel") { _, _ ->
                }.create().show()
            }
        }
    }
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var my_prod_images = itemView.findViewById<ImageView>(R.id.my_prod_icon_img)
        var my_product_name= itemView.findViewById<TextView>(R.id.my_prod_name)
        var my_Product_price = itemView.findViewById<TextView>(R.id.my_prod_price)
        var my_product_description=itemView.findViewById<TextView>(R.id.my_prod_des)
        var my_card = itemView.findViewById<CardView>(R.id.my_prod_card)
        var options=itemView.findViewById<ImageButton>(R.id.options)
    }

}