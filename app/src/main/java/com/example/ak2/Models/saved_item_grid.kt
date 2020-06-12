package com.example.ak2.Models

class saved_item_grid {
    lateinit var item_name: String
    lateinit var item_amt:String
    lateinit var url:String
    lateinit var auct_id: String
    lateinit var count: String
    lateinit var auct_end_date:String
    lateinit var auct_end_time:String
    constructor( item_name: String, item_amt:String,url:String,auct_id:String,count:String) {
        this.item_name= item_name
        this.item_amt =item_amt
        this.url=url
        this.auct_id=auct_id
        this.count=count
        this.auct_end_date=auct_end_date
        this.auct_end_time=auct_end_time
    }
    constructor(){}
}