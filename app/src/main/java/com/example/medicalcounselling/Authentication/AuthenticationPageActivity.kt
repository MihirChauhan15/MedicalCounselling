package com.example.medicalcounselling.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.medicalcounselling.Dao.UsersDao
import com.example.medicalcounselling.MainActivity
import com.example.medicalcounselling.R
import com.example.medicalcounselling.databinding.ActivityAuthenticationPageBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.medicalcounselling.models.Users
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_authentication_page.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.android.synthetic.main.activity_main.*


class AuthenticationPageActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var binding: ActivityAuthenticationPageBinding
    private val RC_SIGN_IN: Int=123
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient =GoogleSignIn.getClient(this,gso)

        binding.buttonPhoneAuth.setOnClickListener {
            startActivity(Intent(this@AuthenticationPageActivity, PhoneNumberActivity::class.java))
        }
        binding.signInButton.setOnClickListener {
            signIn()
        }
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("SignInActivity", "Google sign in failed", e)

            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        progressBar3.visibility= View.VISIBLE
        GlobalScope.launch (Dispatchers.IO){
            val auth =mAuth.signInWithCredential(credential).await()
            val firebaseUser = auth.user
            withContext(Dispatchers.Main){
                updateUI(firebaseUser)
            }

        }

    }

    private fun updateUI(firebaseUser: FirebaseUser?) {

        if(firebaseUser!=null){
            Toast.makeText(this,"Login successful",Toast.LENGTH_SHORT).show()
            val users= firebaseUser.displayName?.let {
                Users(firebaseUser.uid,
                    it,firebaseUser.photoUrl.toString())
            }
            val userDao = UsersDao()
            users?.let { userDao.addUser(users) }
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            signInButton.visibility= View.VISIBLE
            progressBar3.visibility=View.GONE
            Toast.makeText(this,"Please Login Again",Toast.LENGTH_SHORT).show()
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

        startActivity(Intent(this@AuthenticationPageActivity, MainActivity::class.java))
        finish()
    }
}