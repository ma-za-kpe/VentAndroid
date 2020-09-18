package com.makumatthew.vent

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ke.kazinow.app.VentApplication

class SplashActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    val prefManager = VentApplication.instance!!.prefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (prefManager?.isFirstTimeLaunch!!) {
            gotointro()
        } else {
            goToMainActivity()
        }
    }

//    override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = mAuth!!.currentUser
//        updateUI(currentUser)
//    }
//
//    private fun updateUI(currentUser: FirebaseUser?) {
//        if (currentUser!=null){
//            goToMainActivity()
//        } else {
//            gotologin()
//        }
//    }

    private fun gotointro() {
        val myIntent = Intent(this, IntroActivity::class.java)
        this.startActivity(myIntent)
    }

    private fun goToMainActivity() {
        val myIntent = Intent(this, MainActivity::class.java)
        this.startActivity(myIntent)
    }

}