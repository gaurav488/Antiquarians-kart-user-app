package com.example.ak2.Models

class cart_grid {
    lateinit var Prod_images: String
    lateinit var Product_name: String
    lateinit var Product_Price:String
    lateinit var Product_description:String
    lateinit var url: String
    lateinit var Product_id:String
    lateinit var category: String
    constructor(Prod_images: String, Product_name: String, Product_Price :String,url:String,Product_id:String,Product_description:String,category:String) {
        this.Product_name= Product_name
        this.Prod_images = Prod_images
        this.Product_Price =Product_Price
        this.url=url
        this.Product_id=Product_id
        this.Product_description=Product_description
        this.category= category

    }
    constructor(){}
}