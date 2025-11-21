package com.example.muconnect

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.muconnect.databinding.ActivityLoginBinding
import com.example.muconnect.utils.FirebaseUtil
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val studentId = binding.etStudentId.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (studentId.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(studentId, password)
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser(studentId: String, password: String) {
        binding.btnLogin.isEnabled = false

        // Find user by student ID
        FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("studentId", studentId)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "Student ID not found", Toast.LENGTH_SHORT).show()
                    binding.btnLogin.isEnabled = true
                    return@addOnSuccessListener
                }

                val email = documents.documents[0].getString("email") ?: ""

                // Sign in with email and password
                FirebaseUtil.getAuth().signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        updateUserStatus(true)
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        binding.btnLogin.isEnabled = true
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.btnLogin.isEnabled = true
            }
    }

    private fun updateUserStatus(isOnline: Boolean) {
        val userId = FirebaseUtil.getCurrentUserId()
        if (userId.isNotEmpty()) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .update(
                    mapOf(
                        "isOnline" to isOnline,
                        "lastSeen" to System.currentTimeMillis()
                    )
                )
        }
    }
}