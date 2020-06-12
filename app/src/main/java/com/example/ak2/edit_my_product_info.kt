package com.example.ak2

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_edit_my_product_info.*
import kotlinx.android.synthetic.main.activity_sell_item.*
import java.io.IOException
import java.util.*

class edit_my_product_info : AppCompatActivity(), View.OnClickListener {
    private val PICK_IMAGE_REQUEST = 1234
    private var filePath: Uri? = null
    internal var storagefire: FirebaseStorage? = null
    internal var storimg: StorageReference? = null
    lateinit var options: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_my_product_info)
        val categoryname= getIntent().getExtras()?.get("category").toString()
        val prod_id= getIntent().getExtras()?.get("prod_id").toString()
        storagefire = FirebaseStorage.getInstance()
        storimg = storagefire!!.reference
        val btn = findViewById(R.id.edit_choose_product_img) as Button
        btn.setOnClickListener(this)
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val addprod=findViewById(R.id.save_edit_product) as Button
        addprod.setOnClickListener{
            uploadFile()
        }

        options = findViewById(R.id.category_spinner) as Spinner
        val list_of_items = arrayOf("Choose Category", "Arts", "Collectibles", "Decoratives", "Furniture", "Guns", "Rings", "Toys", "Wines")
        options.adapter = ArrayAdapter<String>(this@edit_my_product_info, android.R.layout.simple_spinner_item, list_of_items)


        options.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@edit_my_product_info, "Please select the category", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(parent!!.getItemAtPosition(position).equals("Choose category")){
                    Toast.makeText(this@edit_my_product_info,"Please choose the category",Toast.LENGTH_SHORT).show()
                }
                else{

                }
            }
        }
        val ref = FirebaseDatabase.getInstance().reference.child("Users").child(uid).child("user_product").child(categoryname).child(prod_id)
        ref.addValueEventListener(object : ValueEventListener {


            override fun onDataChange(ds: DataSnapshot) {
                if (ds.exists()) {
                    if (ds.hasChild("url")) {
                        val imgurl = ds.child("url").value.toString()
                        val edit_img=findViewById(R.id.edit_product_img)  as ImageView
                        Glide.with(this@edit_my_product_info).load(imgurl).into(edit_img)
                    }
                    if (ds.hasChild("product_name")) {
                        val pname= ds.child("product_name").value.toString()
                        edit_product_name.setText(pname)
                    }
                    if (ds.hasChild("product_description")) {
                        val pdes = ds.child("product_description").value.toString()
                        edit_product_des.setText(pdes)
                    }
                    if (ds.hasChild("Product_Price")) {
                        var pprice = ds.child("Product_Price").value.toString()
                        edit_product_price.setText(pprice)
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
    override fun onClick(v: View?) {
        if (v === edit_choose_product_img) {
            showfileChooser()
        }
    }override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, filePath)
                val imgv =findViewById(R.id.edit_product_img) as ImageView
                imgv.setImageBitmap(bitmap)

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
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST
        )

    }
    private fun Product_details(it: Task<UploadTask.TaskSnapshot>) {

        val categoryname = getIntent().getExtras()?.get("category").toString()
        val product_name = edit_product_name.text.toString().trim()
        val product_description = edit_product_des.text.toString().trim()
        val product_price = edit_product_price.text.toString().trim()
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val prod_id = getIntent().getExtras()?.get("prod_id").toString()
        val catespin=category_spinner.toString().trim()
        val hashMap = HashMap<String, Any>()
        if (product_name.isNotEmpty()) {
            hashMap["product_name"] = product_name
        }
        if (product_description.isNotEmpty()) {
            hashMap["product_description"] = product_description
        }
        if (product_price.isNotEmpty()) {
            hashMap["Product_Price"] = product_price
        }
       /* if (catespin.isNotEmpty()) {
            hashMap["category"] = catespin
        }*/
        if (it != null) {
            try {
                it.result?.storage?.downloadUrl?.addOnSuccessListener {
                        hashMap["url"] = it.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val prodref = FirebaseDatabase.getInstance().getReference()
            prodref.child("new_product").child(categoryname).child(prod_id).updateChildren(hashMap)
                .addOnCompleteListener {
                    Toast.makeText(applicationContext, " Saved successfully ", Toast.LENGTH_LONG)
                        .show()
                    val userref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                    userref.child("user_product").child(categoryname).child(prod_id)
                        .updateChildren(hashMap)
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


/*    val calen= Calendar.getInstance()
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
                   val hashMap = HashMap<String,String>()
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
*/