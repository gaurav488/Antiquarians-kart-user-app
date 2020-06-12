package com.example.ak2.Models

class current_bid_grid {
    lateinit var auct_id:String
    lateinit var item_name: String
    lateinit var bid_amount:String
    lateinit var current_date_time:String
    lateinit var item_amt:String
    constructor( item_name: String,current_date_time:String,auct_id:String,bid_amount:String,item_amt:String) {
        this.item_name= item_name
        this.current_date_time=current_date_time
        this.bid_amount=bid_amount
        this.auct_id=auct_id
        this.item_amt=item_amt
    }
    constructor(){}
}