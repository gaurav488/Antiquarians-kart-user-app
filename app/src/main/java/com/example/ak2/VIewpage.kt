package com.example.ak2

import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.DialogTitle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_viewpage.*

class VIewpage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpage)

        val adapter = Myadapter(supportFragmentManager)
        adapter.addfragment(Prof(), "Account")
        adapter.addfragment(Setts(), "Settings")
        viewpg.adapter = adapter
        tabl.setupWithViewPager(viewpg)

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
