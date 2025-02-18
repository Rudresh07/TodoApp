package com.example.notessqlite

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()



        Handler().postDelayed({

                startActivity(Intent(this, MainActivity::class.java))

            finish() // finish the splash screen activity to prevent the user from coming back to it using the back button
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}
