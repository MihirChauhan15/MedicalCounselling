package com.example.medicalcounselling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.medicalcounselling.databinding.ActivitySetUpProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SetUpProfileActivity : AppCompatActivity() {
    lateinit var setUpProfileBinding: ActivitySetUpProfileBinding
    lateinit var auth:FirebaseAuth
    lateinit var database:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpProfileBinding = ActivitySetUpProfileBinding.inflate(layoutInflater)
        setContentView(setUpProfileBinding.root)
        setUpProfileBinding.continueButton.setOnClickListener {
            if(setUpProfileBinding.name!=null){
                startActivity(Intent(this@SetUpProfileActivity,MainActivity::class.java))
            }
            else{
                Toast.makeText(this,"Please enter your name",Toast.LENGTH_SHORT).show()
            }
        }
    }
}