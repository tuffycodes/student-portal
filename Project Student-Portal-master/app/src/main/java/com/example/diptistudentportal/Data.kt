package com.example.diptistudentportal

import com.google.firebase.Timestamp

data class Data(
    var id:String? = null,
    var studentid:String? = null,
    val name:String? = null,
    val email:String? = null,
    val subject:String? = null,
    val birthdate:String? = null,
    val timestamp: Timestamp?= null
)
