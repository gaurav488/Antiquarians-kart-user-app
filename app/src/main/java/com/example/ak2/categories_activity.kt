package com.example.ak2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_categories_activity.*

class categories_activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_activity)
        val rings= findViewById<ImageView>(R.id.rings)
        val guns= findViewById<ImageView>(R.id.guns)
        val art= findViewById<ImageView>(R.id.art)
        val furni= findViewById<ImageView>(R.id.furni)
        val wine= findViewById<ImageView>(R.id.wine)
        val deco= findViewById<ImageView>(R.id.deco)
        val toy= findViewById<ImageView>(R.id.toy)
        val collectibles= findViewById<ImageView>(R.id.collectibles)

        rings.setOnClickListener{
        val intent = Intent(applicationContext,sell_item::class.java)
            intent.putExtra("category","Rings")
            startActivity(intent)
        }
        guns.setOnClickListener{
            val intent = Intent(applicationContext,sell_item::class.java)
            intent.putExtra("category","Guns")
            startActivity(intent)
        }
        art.setOnClickListener{
            val intent = Intent(applicationContext,sell_item::class.java)
            intent.putExtra("category","Arts")
            startActivity(intent)
        }
        furni.setOnClickListener{
            val intent = Intent(applicationContext,sell_item::class.java)
            intent.putExtra("category","Furniture")
            startActivity(intent)
        }
        wine.setOnClickListener{
            val intent = Intent(applicationContext,sell_item::class.java)
            intent.putExtra("category","Wines")
            startActivity(intent)
        }
        toy.setOnClickListener{
            val intent = Intent(applicationContext,sell_item::class.java)
            intent.putExtra("category","Toys")
            startActivity(intent)
        }
        deco.setOnClickListener{
            val intent = Intent(applicationContext,sell_item::class.java)
            intent.putExtra("category","Decoratives")
            startActivity(intent)
        }
        collectibles.setOnClickListener{
            val intent = Intent(applicationContext,sell_item::class.java)
            intent.putExtra("category","Collectibles")
            startActivity(intent)
        }


    }


}
