package com.example.ak2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_prof.*

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        Lgin.setOnClickListener() {
            lgin()
        }
        signup2.setOnClickListener() {
            startActivity(Intent(this,Register::class.java))
            finish()
        }
    }
    private fun lgin() {
        val emai = findViewById<View>(R.id.Email) as EditText
        val pa1 = findViewById<View>(R.id.Password) as EditText
        val em2 = emai.text.toString()
        val ps2 = pa1.text.toString()
        if( em2.isEmpty() ||ps2.isEmpty()){
            Toast.makeText(this, "Enter Required Credentials ", Toast.LENGTH_LONG ).show()
            return
        }

        auth.signInWithEmailAndPassword(em2, ps2)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }
    }
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    private  fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
