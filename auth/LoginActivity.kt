package com.rsservice.muconnect.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.rsservice.muconnect.databinding.ActivityLoginBinding
import com.rsservice.muconnect.ui.student.StudentDashboardActivity
import com.rsservice.muconnect.ui.admin.AdminDashboardActivity
import com.rsservice.muconnect.ui.teacher.TeacherDashboardActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener { loginUser() }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvForgot.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun loginUser() {
        val id = binding.etUsername.text.toString().trim().uppercase()
        val dob = binding.etPassword.text.toString().trim()

        if (id.isEmpty() || dob.isEmpty()) {
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show()
            return
        }

        when {
            id.startsWith("S") -> loginStudent(id, dob)
            id.startsWith("T") -> loginTeacher(id, dob)
            id.startsWith("A") -> loginAdmin(id, dob)
            else -> Toast.makeText(this, "Invalid ID format", Toast.LENGTH_SHORT).show()
        }
    }

    // ---------- STUDENT ----------
    private fun loginStudent(id: String, dob: String) {
        db.collection("Users").document("Students")
            .collection("Records").document(id)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    toast("Student details not found")
                    return@addOnSuccessListener
                }
                val storedDob = doc.getString("dob") ?: ""
                if (storedDob == dob) {
                    toast("Student Login Successful")
                    startActivity(Intent(this, StudentDashboardActivity::class.java))
                    finish()
                } else toast("Incorrect DOB")
            }
    }

    // ---------- TEACHER ----------
    private fun loginTeacher(id: String, dob: String) {
        db.collection("Users").document("Teachers")
            .collection("Records").document(id)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    toast("Teacher details not found")
                    return@addOnSuccessListener
                }
                val storedDob = doc.getString("dob") ?: ""
                if (storedDob == dob) {
                    toast("Teacher Login Successful")
                    startActivity(Intent(this, TeacherDashboardActivity::class.java))
                    finish()
                } else toast("Incorrect DOB")
            }
    }

    // ---------- ADMIN ----------
    private fun loginAdmin(id: String, pass: String) {
        db.collection("Users").document("Admins")
            .collection("Records").document(id)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    toast("Admin details not found")
                    return@addOnSuccessListener
                }
                val storedPass = doc.getString("password") ?: ""
                if (storedPass == pass) {
                    toast("Admin Login Successful")
                    startActivity(Intent(this, AdminDashboardActivity::class.java))
                    finish()
                } else toast("Incorrect Password")
            }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
