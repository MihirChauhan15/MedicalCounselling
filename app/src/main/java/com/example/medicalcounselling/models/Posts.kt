package com.example.medicalcounselling.models

data class Posts(
        val id:String="",
        val text: String = "",
        val createdBy: Users = Users(),
        val disease: String = "",
        val therapy: String = "",
        val createdAt: Long = 0L,
        val likedBy: ArrayList<String> = ArrayList(),
        var comments: ArrayList<Comment> = ArrayList()

)