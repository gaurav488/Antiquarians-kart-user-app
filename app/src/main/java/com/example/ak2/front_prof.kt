package com.example.ak2


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_front_prof.view.*

class front_prof : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_front_prof, container, false)
        view.sgup.setOnClickListener {
            startActivity(Intent(context, Register::class.java))
        }
        return view
    }


}
