package com.example.ak2

import android.accounts.Account
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_app_bar.*
class MainActivity : AppCompatActivity() {
    lateinit var HomeFragment: HomeFragment
    lateinit var SearchFragment: SearchFragment
    lateinit var Acccount_prof: Acccount_prof
    lateinit var Auction: AuctionFragment
    lateinit var buyn: buynow
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = "AntiQuarian's Kart"
        actionBar!!.setBackgroundDrawable(resources.getDrawable(R.drawable.shadow_bottom))
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.btm_nav)
        HomeFragment = HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, HomeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Home -> {
                    HomeFragment = HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, HomeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                }
                R.id.Search -> {
                    SearchFragment = SearchFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, SearchFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

                R.id.Auction -> {
                    Auction = AuctionFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, Auction)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.buy -> {
                    buyn = buynow()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, buyn)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.Account -> {
                    Acccount_prof = Acccount_prof()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, Acccount_prof)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
            }
            true
        }
    }
}












