package com.rsservice.muconnect.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rsservice.muconnect.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TEMP UI — you can customize later
        binding.tvTitle.text = "Register Screen"
    }
}
