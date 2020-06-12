package com.example.ak2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_frontpg.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_app_bar.*
class frontpg : AppCompatActivity() {
    lateinit var HomeFrag: HomeFragment
    lateinit var SearchFrag: SearchFragment
    lateinit var Buyn: buynow
    lateinit var frontProf: front_prof
    lateinit var Auction:AuctionFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frontpg)
        setSupportActionBar(toolbar2)
        val actionBar = supportActionBar
        actionBar?.title = "AntiQuarian's Kart"
        val bottomNav: BottomNavigationView = findViewById(R.id.botm_nav2)
        HomeFrag = HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout2, HomeFrag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Home2 -> {
                    HomeFrag = HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout2, HomeFrag)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.Search2 -> {
                    SearchFrag = SearchFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout2, SearchFrag)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

               R.id.Auction2 -> {
                   Auction= AuctionFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout2, Auction)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.frontprof2 -> {
                    frontProf = front_prof()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout2, frontProf)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.Buynow-> {
                Buyn = buynow()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout2, Buyn)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
              }
            }
            true
        }
    }
    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}