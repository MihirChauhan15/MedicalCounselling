package com.example.medicalcounselling.models

data class Posts(
        val text:String="",
        val createdBy: Users = Users(),
        val disease:String ="",
        val therapy:String ="",
        val createdAt:Long=0L,
        val likedBy:ArrayList<String> = ArrayList()
)