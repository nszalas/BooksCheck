package com.nszalas.bookscheck

class ModelBook {

    var id:String = ""
    var bookTitle:String = ""
    var author:String = ""
    var description:String = ""
    var read:Boolean = false
    var timestamp:Long = 0
    var uid:String = ""

    constructor()

    constructor(id: String, bookTitle: String, author: String, description: String, read: Boolean, timestamp: Long, uid: String){
        this.id = id
        this.bookTitle = bookTitle
        this.author = author
        this.description = description
        this.read = read
        this.timestamp = timestamp
        this.uid = uid
    }

}