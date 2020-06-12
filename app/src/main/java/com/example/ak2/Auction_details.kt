package com.example.ak2

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_auction_details.*
import kotlinx.android.synthetic.main.fragment_auction_details.view.*
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Auction_details : Fragment(){
    private val PICK_IMAGE_REQUEST = 1234
    private var filePath: Uri? = null
    internal var storage: FirebaseStorage? = null
    internal var storageref: StorageReference? = null
    var c = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_auction_details, container, false)

        storage = FirebaseStorage.getInstance()
        storageref = storage!!.reference

        val btn = view.findViewById(R.id.btnchoose) as Button
        btn.setOnClickListener{
            showfileChooser()
        }
        view.findViewById(R.id.creatauc) as Button
       val datetime= view.findViewById<Button>(R.id.pick_date)
        datetime.setOnClickListener {

            var timeformat= SimpleDateFormat("dd MMM, yyyy",Locale.getDefault())
            val datepicker = DatePickerDialog(context!!,android.R.style.Theme_Holo_Light_Dialog,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                c.set(Calendar.YEAR,year)
                c.set(Calendar.MONTH,month)
                c.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                val selectformats= timeformat.format(c.time)
                Date.text = selectformats
            },
                c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH))
                datepicker.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                datepicker.show()
            }
        val pick_time= view.findViewById<Button>(R.id.pick_time)
        pick_time.setOnClickListener {
            var timeformat=SimpleDateFormat("hh:mm a")
            val selectedtime=Calendar.getInstance()
            val timepicker= TimePickerDialog(context!!,android.R.style.Theme_Holo_Light_Dialog,TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                selectedtime.set(Calendar.HOUR_OF_DAY,hourOfDay)
                selectedtime.set(Calendar.MINUTE,minute)
                val time = timeformat.format(selectedtime.time)
                pick_end_time.text = time
            },
                c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),false)
                timepicker.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                timepicker.show()
        }
        view.creatauc.setOnClickListener{
            uploadFile()
        }
        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(context?.contentResolver, filePath)
                val imgv = view?.findViewById(R.id.imageView) as ImageView
                imageView.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun uploadFile() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(context)


            val imageref = storageref!!.child("image/" + UUID.randomUUID().toString())
            imageref.putFile(filePath!!)
                .addOnCompleteListener {
                    Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show()
                    auct_detail(it)
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
               /*.addOnProgressListener { taskSnapshot ->
                    val progress =
                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    progressDialog.setMessage("Auction Created" + progress.toInt() + "%..")
                }
            progressDialog.dismiss()*/
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
    private fun auct_detail(it: Task<UploadTask.TaskSnapshot>) {

        if(it!=null)
        {
            try {
                it.result!!.storage.downloadUrl.addOnSuccessListener {
                    val itemn = item_name.text.toString().trim()
                    val itemdes = item_desc.text.toString().trim()
                    val itemdate= Date.text.toString().trim()
                    val itemamt =item_strt_amt.text.toString().trim()
                    val enddate=pick_end_time.text.toString().trim()
                    val estimate_start=item_estimate_strt_amt.text.toString().trim()
                    val estimate_end=item_estimate_end_amt.text.toString().trim()
                    val uid=FirebaseAuth.getInstance().currentUser?.uid.toString()
                    val auctId: String =UUID.randomUUID().toString() //random id generated
                    val hashMap:HashMap<String,String> = HashMap<String,String>()
                    hashMap.put("item_name",itemn)
                    hashMap.put("item_description",itemdes)
                    hashMap.put("item_amt",itemamt)
                    hashMap.put("Uid",uid)
                    hashMap.put("estimated_bid",estimate_start+"-"+estimate_end)
                    hashMap.put("auct_end_date",itemdate)
                    hashMap.put("auct_end_time",enddate)
                    hashMap.put("auct_id",auctId)
                    hashMap.put("url",it.toString())
                    val ref = FirebaseDatabase.getInstance().reference
                    ref.child("auction").child(auctId).setValue(hashMap).addOnCompleteListener {
                        Toast.makeText(context, " Saved successfully ", Toast.LENGTH_LONG).show()
                    }
                }
            }catch (e: Exception){
               e.printStackTrace()
            }
        }
    }

}

