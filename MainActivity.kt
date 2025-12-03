package com.rsservice.muconnect

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // Android 12+ official splash screen
        installSplashScreen()

        super.onCreate(savedInstanceState)

        // Open your animated splash screen
        startActivity(
            Intent(this, com.rsservice.muconnect.auth.SplashActivity::class.java)
        )

        finish()
    }
}
