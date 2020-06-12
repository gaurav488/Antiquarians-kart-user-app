package com.example.ak2

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_sell_item.*
import kotlinx.android.synthetic.main.fragment_auction_details.*
import kotlinx.android.synthetic.main.fragment_auction_details.view.*
import java.io.IOException
import java.util.*

class sell_item : AppCompatActivity(), View.OnClickListener {
    private val PICK_IMAGE_REQUEST = 1234
    private var filePath: Uri? = null
    internal var storagefire: FirebaseStorage? = null
    internal var storimg: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_item)
        storagefire = FirebaseStorage.getInstance()
        storimg = storagefire!!.reference
        val btn = findViewById(R.id.choose_product_img) as Button
       choose_product_img.setOnClickListener(this)
       val addprod=findViewById(R.id.add_product) as Button
      addprod.setOnClickListener{
            uploadFile()
        }
    }
    override fun onClick(v: View?) {
        if (v === choose_product_img) {
            showfileChooser()
        }
    }override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, filePath)
                val imgv =findViewById(R.id.product_img) as ImageView
                product_img.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
    private fun uploadFile() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(applicationContext)
            val imageref = storimg!!.child("image/" + UUID.randomUUID().toString())
            imageref.putFile(filePath!!)
                .addOnCompleteListener {
                    Toast.makeText(applicationContext, "Uploaded", Toast.LENGTH_SHORT).show()
                    Product_details(it)
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                }

        }
    }
    private fun showfileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE_REQUEST
        )
    }
    private fun Product_details(it: Task<UploadTask.TaskSnapshot>) {
        val calen= Calendar.getInstance()
        val currentdate= java.text.SimpleDateFormat("dd MMM, yyy")
        val savecurrent=currentdate.format(calen.time)
        val currenttime= java.text.SimpleDateFormat("hh:mm a")
        val savecurrenttime= currenttime.format(calen.time)
        val categoryname= getIntent().getExtras()?.get("category").toString()
        if(it!=null)
        {
            try {
                it.result?.storage?.downloadUrl?.addOnSuccessListener {
                    val product_name = product_name.text.toString().trim()
                    val product_description = product_des.text.toString().trim()
                    val product_price =product_price.text.toString().trim()
                    val uid= FirebaseAuth.getInstance().currentUser?.uid.toString()
                    val product_id: String =UUID.randomUUID().toString() //random id generated
                    val hashMap:HashMap<String,String> = HashMap<String,String>()
                    hashMap.put("Uid",uid)
                    hashMap.put("product_name",product_name)
                    hashMap.put("product_description",product_description)
                    hashMap.put("Product_Price",product_price)
                    hashMap.put("product_id",product_id)
                    hashMap.put("date",savecurrent)
                    hashMap.put("time",savecurrenttime)
                    hashMap.put("category", categoryname.toString())
                    hashMap.put("url",it.toString())
                    val ref = FirebaseDatabase.getInstance().getReference()
                    ref.child("new_product").child(categoryname).child(product_id).setValue(hashMap).addOnCompleteListener {
                        Toast.makeText(applicationContext, " Saved successfully ", Toast.LENGTH_LONG).show()
                        val userref=FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                        userref.child("user_product").child(categoryname).child(product_id).setValue(hashMap)
                    }
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        startActivity(Intent(this,my_products::class.java))
    }
}
/*.addOnProgressListener { taskSnapshot ->
                val progress =
                    100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                progressDialog.setMessage("Auction Created" + progress.toInt() + "%..")
            }
        progressDialog.dismiss()*/