package com.example.medicalcounselling.Authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicalcounselling.Dao.UsersDao
import com.example.medicalcounselling.MainActivity
import com.example.medicalcounselling.databinding.ActivityOTPBinding
import com.example.medicalcounselling.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.*


class OTPActivity : AppCompatActivity() {
    lateinit var binding: ActivityOTPBinding
    lateinit var mAuth: FirebaseAuth
    lateinit var verificationId:String
    val IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/medical-counselling.appspot.com/o/avatar.png?alt=media&token=1c32b88e-5648-409e-afeb-38aaabf3c0ec";

    val TAG:String ="OTPActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOTPBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance();

        val phoneNumber: String? =intent.getStringExtra("PhoneNumber")

        binding.phnLabel.setText("Verify $phoneNumber")

        val otp: String? =intent.getStringExtra("OTP")
        binding.continueButton1.setOnClickListener {
            binding.progressBar2.visibility = View.VISIBLE
            verificationId=binding.otpView.text.toString().trim()
            if (!verificationId.isEmpty()) {
                val credential = otp?.let { it1 -> PhoneAuthProvider.getCredential(it1, verificationId) }
                if (credential != null) {
                    signInWithPhoneAuthCredential(credential)
                }
            } else {
                Toast.makeText(this@OTPActivity, "Please Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }



    }
    override fun onStart() {
        super.onStart()
        val currentUser= mAuth.currentUser
        if (currentUser != null) {
            sendToMain()
        }
    }

    private fun sendToMain() {
        startActivity(Intent(this@OTPActivity, MainActivity::class.java))
        finish()
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    binding.progressBar2.visibility = View.INVISIBLE
//                    var uniqueID = UUID.randomUUID().toString()
//                    val users= Users(uniqueID,
//                            "Anonymous",IMAGE_URL)
//                    val userDao = UsersDao();
//                    userDao.addUser(users);
                    sendToMain()
                    Toast.makeText(applicationContext, "Logged In successful", Toast.LENGTH_SHORT).show()
                } else {
                    binding.progressBar2.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, "Logged In failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
