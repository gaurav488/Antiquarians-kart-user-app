package com.example.ak2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_password.*

class change_password : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        auth = FirebaseAuth.getInstance()
        confirm_pass.setOnClickListener {
            changepass()
        }
    }
    private fun changepass() {
        if (current_pass.text.toString().isNotEmpty() && new_pass.text.toString().isNotEmpty() &&
            confirm_pass.text.toString().isNotEmpty()
        ) {
            if(new_pass.text.toString().equals(confirm_pass.text.toString())){
                val user=auth.currentUser
                    if(user!=null && user.email!=null){
                        val credential = EmailAuthProvider
                            .getCredential(user.email!!,current_pass.text.toString())


                        user?.reauthenticate(credential)
                            ?.addOnCompleteListener {
                                if(it.isSuccessful){
                                    Toast.makeText(applicationContext,"Password Changed",Toast.LENGTH_SHORT).show()
                                    user?.updatePassword(new_pass.text.toString())
                                        ?.addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(applicationContext,"Password Changed Successfully",Toast.LENGTH_SHORT).show()
                                                auth.signOut()
                                                startActivity(Intent(this,Login::class.java))
                                                finish()
                                            }
                                        }
                                }else{
                                    Toast.makeText(applicationContext,"Password change failed",Toast.LENGTH_SHORT).show()
                                }
                            }
                    }else{
                        startActivity(Intent(this,Login::class.java))
                        finish()
                    }
            }else{
                Toast.makeText(applicationContext,"Password Mismatch",Toast.LENGTH_SHORT).show()
            }

        } else {
        Toast.makeText(applicationContext,"Please enter fields",Toast.LENGTH_SHORT).show()
        }
    }
}
