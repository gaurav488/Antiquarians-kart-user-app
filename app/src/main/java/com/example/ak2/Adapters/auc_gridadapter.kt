package com.example.ak2.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.ak2.Models.auc_grid
import com.example.ak2.R
import com.example.ak2.auction_item_info
import com.example.ak2.new_product_info
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class auc_gridadapter(var context: Context?, var arr_List: ArrayList<auc_grid>) :
    RecyclerView.Adapter<auc_gridadapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.auc_grid, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return arr_List.size
    }

fun update(list:ArrayList<auc_grid>){
    this.arr_List=list
    notifyDataSetChanged()
}

    override fun onBindViewHolder(holder: auc_gridadapter.ItemHolder, position: Int) {
        val GridItem: auc_grid = arr_List.get(position)
        val circularProgressDrawable = CircularProgressDrawable(context!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide.with(context!!).load(GridItem.url).dontAnimate().placeholder(circularProgressDrawable).into(holder.icons)
        holder.itname.text = GridItem.item_name
        holder.itamt.text = "Price : "+GridItem.item_amt
        holder.itdur.text = "Ends On :"+GridItem.auct_end_date+""+","+""+GridItem.auct_end_time
        holder.card.setOnClickListener{
                FirebaseDatabase.getInstance().getReference().child("auction").child(GridItem.auct_id.toString()).addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                        }
                        override fun onDataChange(p0: DataSnapshot) {
                            if(p0.child("count").value!=null)
                            {
                                var count = (p0.child("count").value.toString().toInt()) + 1
                                Log.d("count", count.toString())
                                val hashmap = HashMap<String, Any>()
                                hashmap.put("count", count.toString())
                                FirebaseDatabase.getInstance().getReference().child("auction")
                                    .child(GridItem.auct_id.toString()).updateChildren(hashmap)
                            }
                            else {
                                var count=1
                                val hashmap = HashMap<String, Any>()
                                hashmap["count"] = count
                                FirebaseDatabase.getInstance().getReference().child("auction").child(GridItem.auct_id.toString())
                                    .updateChildren(hashmap)
                            }
                        }
                    })
           /* var amt:String=GridItem.item_amt.toString()
            var name:String=GridItem.item_name.toString()
            var url:String=GridItem.url.toString()
            var id:String=GridItem.auct_id.toString()*/
            val intent = Intent(context, auction_item_info::class.java)
            intent.putExtra("item_amt", GridItem.item_amt)
            intent.putExtra("auct_id",GridItem.auct_id )
            intent.putExtra("itemname",GridItem.item_name)
            Log.d("disp9990",GridItem.toString())
            intent.putExtra("url", GridItem.url)
            context!!.startActivity(intent)
           // pass(amt,id,name,url)
        }
        val uid= FirebaseAuth.getInstance().currentUser?.uid.toString()
        FirebaseDatabase.getInstance().getReference("auction").child(GridItem.auct_id.toString())
            .child("likes").addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for(ds:DataSnapshot in p0.children)
                    {
                        Log.d("likes",ds.child("Uid").value.toString())
                        if(ds.child("Uid").value.toString()?.equals(uid)!!)
                        {
                            holder.like_img.setImageDrawable(context!!.getDrawable(R.drawable.auct_like))
                        }
                    }
                }
            })
        holder.card.setOnLongClickListener {
            true
        }
        holder.like_img.setOnClickListener{
            holder.like_img.setImageDrawable(context!!.getDrawable(R.drawable.auct_like))
            val data:HashMap<String,String> = HashMap<String,String>()
            data.put("Uid",uid)
            val firedb=FirebaseDatabase.getInstance().getReference("auction").
                child(GridItem.auct_id.toString()).child("likes")
                firedb.child(uid).setValue(data)
            val data2:HashMap<String,String> = HashMap<String,String>()
            data2.put("Uid",uid)
            data2.put("auct_id",GridItem.auct_id.toString())
            data2.put("item_amt",GridItem.item_amt.toString())
            data2.put("item_name",GridItem.item_name.toString())
            data2.put("auct_end_date",GridItem.auct_end_date.toString())
            data2.put("auct_end_time",GridItem.auct_end_time.toString())
            data2.put("url",GridItem.url.toString())
            val fir=FirebaseDatabase.getInstance().getReference("saved_items")
                fir.child(uid).child(GridItem.auct_id).setValue(data2)
        }
    }
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icons = itemView.findViewById<ImageView>(R.id.icon_img)
        var itname = itemView.findViewById<TextView>(R.id.it_nm)

        var itdur = itemView.findViewById<TextView>(R.id.it_dur)
        var itamt = itemView.findViewById<TextView>(R.id.it_amt)
        var card = itemView.findViewById<CardView>(R.id.auct_card)
        var like_img=itemView.findViewById<ImageView>(R.id.like_img)
    }
/*private fun pass(amt:String,auct:String,itname:String,urls:String){
    val intent = Intent(context, auction_item_info::class.java)
    intent.putExtra("item_amt", amt)
    intent.putExtra("auct_id",auct )
    intent.putExtra("itemname",itname)
    Log.d("disp99",itname.toString())
    intent.putExtra("url", urls)
    context!!.startActivity(intent)
}*/
}