package com.example.ak2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_prof.*
import kotlinx.android.synthetic.main.fragment_prof.view.*
import kotlin.collections.HashMap

class Prof : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_prof, container, false)
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        (activity as AppCompatActivity).supportActionBar?.setTitle("Profile")

        val svebtn = view.findViewById<Button>(R.id.savedata)
        svebtn.setOnClickListener {
            save()
        }
        view.chng.setOnClickListener {
            startActivity(Intent(context, change_password::class.java))
        }
        view.rel1.setOnClickListener {
            android.app.AlertDialog.Builder(context).apply {
                setTitle("Are you Sure you want to Logout?")
                setPositiveButton("OK") { _, _ ->
                    LogOut()
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                }
                setNegativeButton("Cancel") { _, _ ->
                }
            }.create().show()

        }

        val ref = FirebaseDatabase.getInstance().reference.child("Users").child(uid)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                if (ds.exists()) {
                    if (ds.hasChild("email")) {
                        val em = ds.child("email").value.toString()
                        Em1.setText(em)
                    }
                    if (ds.hasChild("fname")) {
                        val fname = ds.child("fname").value.toString()
                        Fn.setText(fname)
                    }
                    if (ds.hasChild("lname")) {
                        val ln = ds.child("lname").value.toString()
                        Last.setText(ln)
                    }
                    if (ds.hasChild("address")) {
                        var adr = ds.child("address").value.toString()
                        Adrs.setText(adr)
                    }
                    if (ds.hasChild("region")) {
                        var reg = ds.child("region").value.toString()
                        Reg1.setText(reg)

                    }
                    if (ds.hasChild("city")) {
                        var cty = ds.child("city").value.toString()
                        City.setText(cty)
                    }
                    if (ds.hasChild("state")) {
                        var state = ds.child("state").value.toString()
                        State.setText(state)
                    }
                    if (ds.hasChild("zip")) {
                        var zip = ds.child("zip").value.toString()
                        Zip.setText(zip)
                    }
                    if (ds.hasChild("username")) {
                        var unm2 = ds.child("username").value.toString()
                        unm.setText(unm2)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
        return view
    }



    private fun LogOut() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(context, "Logged Out!", Toast.LENGTH_LONG).show()
        startActivity(Intent(context, frontpg::class.java))
    }

    private fun save() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val FirstName = Fn.text.toString().trim()
        val region = Reg1.text.toString().trim()
        val LastN = Last.text.toString().trim()
        val Address = Adrs.text.toString().trim()
        val state = State.text.toString().trim()
        val zip = Zip.text.toString().trim()
        val city = City.text.toString().trim()
        val email = Em1.text.toString().trim()
        val hashMap = HashMap<String, Any>()
        if (region.isNotEmpty()) {
            hashMap["region"] = region
        }
        if (email.isNotEmpty()) {
            hashMap["email"] = email
        }
        if (FirstName.isNotEmpty()) {
            hashMap["fname"] = FirstName
        }
        if (LastN.isNotEmpty()) {
            hashMap["lname"] = LastN
        }
        if (state.isNotEmpty()) {
            hashMap["state"] = state
        }
        if (city.isNotEmpty()) {
            hashMap["city"] = city
        }
        if (zip.isNotEmpty()) {
            hashMap["zip"] = zip
        }
        if (Address.isNotEmpty()) {
            hashMap["address"] = Address
        }
        if (email.isNotEmpty()) {
            FirebaseAuth.getInstance().currentUser?.updateEmail(email)!!.addOnSuccessListener {
                Log.d("fsdfs", "sgfsdg")
                val ref = FirebaseDatabase.getInstance().getReference("Users").child(uid)
                if (hashMap.isNotEmpty()) {
                    ref.updateChildren(hashMap).addOnCompleteListener {
                        Toast.makeText(context, " Saved successfully ", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

}








