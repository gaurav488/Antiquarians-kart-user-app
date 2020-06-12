package com.example.ak2


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        val view=  inflater.inflate(R.layout.fragment_search, container, false)
        (activity as AppCompatActivity).supportActionBar?.setTitle("Categories")
        val rings = view.findViewById<Button>(R.id.search_rings)
        val guns = view.findViewById<Button>(R.id.search_guns)
        val furniture = view.findViewById<Button>(R.id.search_furniture)
        val wines = view.findViewById<Button>(R.id.search_wines)
        val toys = view.findViewById<Button>(R.id.search_toys)
        val deco = view.findViewById<Button>(R.id.search_deco)
        val collectible = view.findViewById<Button>(R.id.search_collectible)
        val art = view.findViewById<Button>(R.id.search_art)

        rings.setOnClickListener{
          rings()
            }
        toys.setOnClickListener{
            toys()
        }
        guns.setOnClickListener{
           guns()
        }
        furniture.setOnClickListener{
           furniture()
        }
        collectible.setOnClickListener{
           collectibles()
        }
        wines.setOnClickListener{
            wines()
        }

        art.setOnClickListener{
           art()
        }
        deco.setOnClickListener{
            deco()
        }
        return view
    }
    private fun rings(){
        val intent = Intent(context,search_activity::class.java)
        intent.putExtra("category","Rings")
       startActivity(intent)
    }
    private fun toys(){
        val intent = Intent(context,search_activity::class.java)
        intent.putExtra("category","Toys")
        startActivity(intent)
    }
    private fun guns(){
        val intent = Intent(context,search_activity::class.java)
        intent.putExtra("category","Guns")
        startActivity(intent)
    }
    private fun furniture(){
        val intent = Intent(context,search_activity::class.java)
        intent.putExtra("category","Furniture")
        startActivity(intent)
    }
    private fun collectibles(){
        val intent = Intent(context,search_activity::class.java)
        intent.putExtra("category","Collectibles")
        startActivity(intent)
    }
    private fun wines(){
        val intent = Intent(context,search_activity::class.java)
        intent.putExtra("category","Wines")
        startActivity(intent)
    }
    private fun art(){
        val intent = Intent(context,search_activity::class.java)
        intent.putExtra("category","Arts")
        startActivity(intent)
    }
    private fun deco(){
        val intent = Intent(context,search_activity::class.java)
        intent.putExtra("category","Decoratives")
        startActivity(intent)
    }
}
