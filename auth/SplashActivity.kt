package com.rsservice.muconnect.auth

import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.rsservice.muconnect.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        runLogoAnimation()
    }

    private fun runLogoAnimation() {

        // 1) Fade + Scale in logo
        binding.logoView.apply {
            scaleX = 0.4f
            scaleY = 0.4f
            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(700)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }

        // 2) Gold "glow pulse"
        binding.logoView.postDelayed({
            binding.logoView.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(250)
                .withEndAction {
                    binding.logoView.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .duration = 200
                }
        }, 600)

        // 3) Text Fade In
        binding.logoText.postDelayed({
            binding.logoText.animate()
                .alpha(1f)
                .setDuration(500)
                .start()
        }, 900)

        // 4) Move to LoginActivity after animation
        binding.rootLayout.postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 1700)
    }
}
