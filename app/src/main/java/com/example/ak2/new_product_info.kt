package com.example.ak2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_new_product_info.*
import kotlinx.android.synthetic.main.activity_sell_item.*

import kotlin.collections.HashMap


class new_product_info : AppCompatActivity() {

    val current_uid=FirebaseAuth.getInstance().currentUser?.uid.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_product_info)
       val prodid= getIntent().getExtras()?.get("pid").toString()
        val aucuseId= getIntent().getExtras()?.get("Uid").toString()
        Log.d("get",prodid)
        val cate= getIntent().getExtras()?.get("category").toString()
        val dbRef = FirebaseDatabase.getInstance().getReference().child("new_product").child(cate).child(prodid)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                var prodname = p0.child("product_name").value.toString()
                var proddes = p0.child("product_description").value.toString()
                var url = p0.child("url").value.toString()
                var prodprice = p0.child("Product_Price").value.toString()
                pname.setText("Name : "+prodname)
                pprice.setText("Price : "+prodprice)
                pdes.setText("Description : "+proddes)
                Glide.with(applicationContext).load(url).dontAnimate()
                    .into(product_desc_img)
            }
        })
        val uid= FirebaseAuth.getInstance().currentUser?.uid.toString()
       addtocart.setOnClickListener{
           if(uid!=aucuseId){
               addtocart()
           }
           else{
               Toast.makeText(applicationContext,"You cannot add this item to your cart",Toast.LENGTH_SHORT).show()
           }
         }
    }
    private fun addtocart(){
        val prodid= getIntent().getExtras()?.get("pid").toString()
        val cate= getIntent().getExtras()?.get("category").toString()
        val dbRef = FirebaseDatabase.getInstance().getReference().child("new_product").child(cate).child(prodid)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                var prodname = p0.child("product_name").value.toString()
                var proddes = p0.child("product_description").value.toString()
                var prodprice = p0.child("Product_Price").value.toString()
                var url = p0.child("url").value.toString()
                val Uid= p0.child("Uid").value.toString()
                val hashMap:HashMap<String,String> = HashMap<String,String>()
                hashMap.put("Product_name",prodname)
                hashMap.put("Product_description",proddes)
                hashMap.put("Product_Price",prodprice)
                hashMap.put("user_Id",Uid)
                hashMap.put("url",url)
                hashMap.put("Product_id",prodid)
                hashMap.put("category",cate)
                val cartdbref= FirebaseDatabase.getInstance().getReference("Cart").child("Users").child(current_uid).child(prodid)
                cartdbref.setValue(hashMap).addOnCompleteListener {
                    Toast.makeText(applicationContext, " Saved successfully ", Toast.LENGTH_LONG).show()
                }
            }
        })

        val intent= Intent(applicationContext,cart_activity::class.java)
        intent.putExtra("prodid", prodid)
        intent.putExtra("category", cate)
        startActivity(intent)
    }

}

