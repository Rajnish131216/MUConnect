package com.example.muconnect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.muconnect.databinding.ActivityProfileBinding
import com.example.muconnect.models.User
import com.example.muconnect.utils.FirebaseUtil

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        loadUserProfile()
    }

    private fun loadUserProfile() {
        val userId = FirebaseUtil.getCurrentUserId()

        FirebaseUtil.getFirestore()
            .collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                user?.let {
                    binding.tvName.text = it.name
                    binding.tvStudentId.text = "Student ID: ${it.studentId}"
                    binding.tvEmail.text = it.email
                    binding.tvDepartment.text = "Department: ${it.department}"
                    binding.tvYear.text = "Year: ${it.year}"

                    if (it.profileImageUrl.isNotEmpty()) {
                        Glide.with(this)
                            .load(it.profileImageUrl)
                            .into(binding.ivProfile)
                    }
                }
            }
    }
}