package com.example.ak2.Models

class account_grid {
    lateinit var title: String
     var left_icon:Int?=0
     var right_icon:Int?=0
    constructor( title: String,left_icon:Int?,right_icon:Int?) {
        this.title= title
        this.left_icon=left_icon
        this.right_icon= right_icon

    }
    constructor(){}
}
