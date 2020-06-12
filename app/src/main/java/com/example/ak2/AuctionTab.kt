package com.example.ak2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.ak2.*
import kotlinx.android.synthetic.main.activity_auction_tab.*
import kotlinx.android.synthetic.main.activity_viewpage.*
import kotlinx.android.synthetic.main.activity_viewpage.viewpg



class AuctionTab : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auction_tab)
        val adapter = Myadapter(supportFragmentManager)
        adapter.addfragment(current_bid(), "Active Bid")
        adapter.addfragment(past_bid(), "Past Bid")
        viewpg2.adapter = adapter
        tab2.setupWithViewPager(viewpg2)
    }


    class Myadapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val fragmentlist: MutableList<Fragment> = ArrayList()
        private val titleList: MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragmentlist[position]
        }

        override fun getCount(): Int {
            return fragmentlist.size
        }

        fun addfragment(fragment: Fragment, title: String) {
            fragmentlist.add(fragment)
            titleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]

        }
    }
}