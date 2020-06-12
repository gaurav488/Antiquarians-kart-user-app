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
import com.example.ak2.Models.home_grid
import com.example.ak2.Models.saved_item_grid
import com.example.ak2.R
import com.example.ak2.auction_item_info
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class saved_item_adapter (var context: Context?, var arrayList: ArrayList<saved_item_grid>) :
    RecyclerView.Adapter<saved_item_adapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_item_layout, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
    fun update(list:ArrayList<saved_item_grid>){
        this.arrayList=list
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: saved_item_adapter.ItemHolder, position: Int){
        val GridItem: saved_item_grid = arrayList.get(position)

        Glide.with(context!!).load(GridItem.url).placeholder(R.drawable.hammerauct).into(holder.icons)
        holder.titles.text = GridItem.item_name
        holder.price.text = GridItem.item_amt
        holder.card.setOnClickListener {
            val intent = Intent(context, auction_item_info::class.java)
            intent.putExtra("auct_id", GridItem.auct_id)
            intent.putExtra("item_amt", GridItem.item_amt)
            intent.putExtra("itemname", GridItem.item_name)
            it.context.startActivity(intent)
        }

    }
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icons = itemView.findViewById<ImageView>(R.id.saved_icon_img)
        var titles = itemView.findViewById<TextView>(R.id.saved_it_nm)
        var price = itemView.findViewById<TextView>(R.id.saved_it_amt)
        var card = itemView.findViewById<CardView>(R.id.saved_auct_card)
        var likes=itemView.findViewById<ImageView>(R.id.like_img_save)
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
// val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
/*  FirebaseDatabase.getInstance().getReference("auction").
          child(GridItem.auct_id.toString()).child("likes").addValueEventListener(object :
          ValueEventListener {
          override fun onCancelled(p0: DatabaseError) {
          }

          override fun onDataChange(p0: DataSnapshot) {
              for(ds: DataSnapshot in p0.children)
              {
                  if(ds.hasChild(uid))
                      holder.likes.setImageDrawable(context!!.getDrawable(R.drawable.auct_like))
                  else
                      holder.likes.setImageDrawable(context!!.getDrawable(R.drawable.auc_dislike))
              }
          }

      })*/