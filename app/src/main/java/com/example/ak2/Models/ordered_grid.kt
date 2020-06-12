package com.example.ak2.Models

class ordered_grid {
    lateinit var prod_images: String
    lateinit var product_name: String
    lateinit var Product_Price:String
    lateinit var product_description:String
    lateinit var url: String
    lateinit var product_id:String
    lateinit var category: String
    lateinit var Uid:String
    lateinit var orderers_address:String
    lateinit var orderers_cityname:String
    lateinit var orderers_name: String
    lateinit var  orderers_phone:String
    constructor(prod_images: String, product_name: String, Product_Price :String,url:String,
                product_id:String,product_description:String,category: String,Uid:String,
                orderers_phone:String,orderers_name: String,orderers_cityname:String,orderers_address:String) {
        this.product_name= product_name
        this.prod_images = prod_images
        this.Product_Price =Product_Price
        this.url=url
        this.product_id=product_id
        this.product_description=product_description
        this.category= category
        this.Uid=Uid
        this.orderers_address=orderers_address
        this.orderers_cityname=orderers_cityname
        this.orderers_name=orderers_name
        this.orderers_phone=orderers_phone

    }
    constructor(){}
}