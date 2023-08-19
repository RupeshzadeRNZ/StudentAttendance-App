package com.example.studentattend.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.studentattend.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY: Long = 3000 // 3 seconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            // Start the main activity and finish this activity
            val intent = Intent(this@SplashActivity, LogIn::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DELAY)
    }
}