package com.example.medicalcounselling.Authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicalcounselling.Authentication.OTPActivity
import com.example.medicalcounselling.SetUpProfileActivity
import com.example.medicalcounselling.databinding.ActivityPhoneNumberBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneNumberActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var phoneNumberBinding: ActivityPhoneNumberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phoneNumberBinding = ActivityPhoneNumberBinding.inflate(this.layoutInflater)
        setContentView(phoneNumberBinding.root)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()
        phoneNumberBinding.phoneBox.requestFocus()

        phoneNumberBinding.continueButton.setOnClickListener {
            val phoneNumber:String =phoneNumberBinding.phoneBox.text.toString().trim()
            if(phoneNumber!=null){
                val options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91$phoneNumber")       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this@PhoneNumberActivity)                 // Activity (for callback binding)
                        .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                phoneNumberBinding.progressBar.visibility=View.VISIBLE
                                signInWithPhoneAuthCredential(credential)
                                Toast.makeText(applicationContext, "verified", Toast.LENGTH_SHORT).show()
                            }

                            override fun onVerificationFailed(p0: FirebaseException) {
                                Toast.makeText(applicationContext, "${p0.localizedMessage}", Toast.LENGTH_SHORT).show()
                            }

                            override fun onCodeSent(
                                    verifyId: String,
                                    forceResendingToken: PhoneAuthProvider.ForceResendingToken
                            ) {
                                super.onCodeSent(verifyId, forceResendingToken)

                                val intent = Intent(this@PhoneNumberActivity, OTPActivity::class.java)
                                intent.putExtra("PhoneNumber",phoneNumber)
                                intent.putExtra("OTP",verifyId)
                                phoneNumberBinding.progressBar.visibility =View.INVISIBLE
                                startActivity(intent)

                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build()
                PhoneAuthProvider.verifyPhoneNumber(options)

            }else{
                Toast.makeText(applicationContext, "Enter Mobile Number", Toast.LENGTH_SHORT).show()
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
        startActivity(Intent(this@PhoneNumberActivity, SetUpProfileActivity::class.java))
        finish()
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //Toast.makeText(applicationContext, "Logged In successful", Toast.LENGTH_SHORT).show()
                    } else {
                        //Toast.makeText(applicationContext, "Logged In failed", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}