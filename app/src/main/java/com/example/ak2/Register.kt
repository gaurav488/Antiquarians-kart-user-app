package com.example.ak2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.ak2.Login
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.Email
import java.util.regex.Pattern

class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        lg.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
        register.setOnClickListener {
            signUp()
        }
    }

    private fun signUp(){
        val full = findViewById<View>(R.id.UserName) as EditText
        val ps1 = findViewById<View>(R.id.Password) as EditText
        val em1 = findViewById<View>(R.id.Email) as EditText
        val pass1 = findViewById<View>(R.id.reenter) as EditText
        val f1 = full.text.toString()
        val pss1 = ps1.text.toString()
        val emm1 = em1.text.toString()
        val pss2 = pass1.text.toString()

        if( f1.isEmpty() ||pss1.isEmpty() || emm1.isEmpty() || pss2.isEmpty()){
            Toast.makeText(this, "Enter Required Credentials ", Toast.LENGTH_LONG ).show()
        }
        if(pss1.equals(pss2)){
            Toast.makeText(applicationContext,"Password Matched",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(applicationContext,"Password Mismatch",Toast.LENGTH_SHORT).show()
        }


     auth.createUserWithEmailAndPassword(emm1,pss1)
         .addOnCompleteListener(this) { task ->
             if (task.isSuccessful) {
                 emai(emm1)
                startActivity(Intent(this,MainActivity::class.java))
                 finish()
                         }
             else {
                 Toast.makeText(baseContext, "Signup failed try again",
                     Toast.LENGTH_SHORT).show()
             }


         }

    }
    private fun emai( e :String){
        val email_id=e
        var nuid=FirebaseAuth.getInstance().currentUser?.uid.toString()
        val hashMap:HashMap<String,String> = HashMap<String,String>()
        hashMap.put("email",email_id)
        val ref = FirebaseDatabase.getInstance().getReference("Users").child(nuid)
        ref.setValue(hashMap).addOnCompleteListener(){
            Toast.makeText(this, " Saved successfully ", Toast.LENGTH_LONG).show()
        }
        val full = findViewById<View>(R.id.UserName) as EditText
        val f1 = full.text.toString()
        val uid=FirebaseAuth.getInstance().currentUser?.uid.toString()
        val userdb=FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
        val hashmap = HashMap<String, Any>()
        hashmap["username"] = f1
        userdb.updateChildren(hashmap)


    }

}



