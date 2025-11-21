package com.example.muconnect

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.muconnect.databinding.ActivityRegisterBinding
import com.example.muconnect.models.User
import com.example.muconnect.utils.FirebaseUtil

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinners()

        binding.btnRegister.setOnClickListener {
            val studentId = binding.etStudentId.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val name = binding.etName.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val department = binding.spinnerDepartment.selectedItem.toString()
            val year = binding.spinnerYear.selectedItem.toString()

            if (studentId.isEmpty() || email.isEmpty() || name.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!email.endsWith("@example.edu")) {
                Toast.makeText(this, "Please use example email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(studentId, email, name, password, department, year)
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun setupSpinners() {
        val departments = arrayOf("Computer Science", "Electronics", "Mechanical", "Civil", "IT")
        val years = arrayOf("First Year", "Second Year", "Third Year", "Fourth Year")

        binding.spinnerDepartment.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            departments
        )

        binding.spinnerYear.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            years
        )
    }

    private fun registerUser(
        studentId: String,
        email: String,
        name: String,
        password: String,
        department: String,
        year: String
    ) {
        binding.btnRegister.isEnabled = false

        FirebaseUtil.getAuth().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: ""

                val user = User(
                    uid = uid,
                    studentId = studentId,
                    email = email,
                    name = name,
                    department = department,
                    year = year,
                    isOnline = true
                )

                FirebaseUtil.getFirestore()
                    .collection("users")
                    .document(uid)
                    .set(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to save user: ${e.message}", Toast.LENGTH_SHORT).show()
                        binding.btnRegister.isEnabled = true
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Registration failed: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.btnRegister.isEnabled = true
            }
    }
}