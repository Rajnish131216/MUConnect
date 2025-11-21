package com.example.muconnect

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muconnect.adapters.UserAdapter
import com.example.muconnect.databinding.ActivityHomeBinding
import com.example.muconnect.models.User
import com.example.muconnect.utils.FirebaseUtil
import com.google.firebase.firestore.ListenerRegistration

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userAdapter: UserAdapter
    private val usersList = mutableListOf()
    private var usersListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setupRecyclerView()
        loadUsers()
        updateUserStatus(true)
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(usersList) { user ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }

        binding.recyclerViewUsers.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = userAdapter
        }
    }

    private fun loadUsers() {
        val currentUserId = FirebaseUtil.getCurrentUserId()

        usersListener = FirebaseUtil.getFirestore()
            .collection("users")
            .whereNotEqualTo("uid", currentUserId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                usersList.clear()
                snapshot?.documents?.forEach { doc ->
                    val user = doc.toObject(User::class.java)
                    user?.let { usersList.add(it) }
                }
                userAdapter.notifyDataSetChanged()
            }
    }

    private fun updateUserStatus(isOnline: Boolean) {
        val userId = FirebaseUtil.getCurrentUserId()
        FirebaseUtil.getFirestore()
            .collection("users")
            .document(userId)
            .update(
                mapOf(
                    "isOnline" to isOnline,
                    "lastSeen" to System.currentTimeMillis()
                )
            )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            R.id.action_logout -> {
                updateUserStatus(false)
                FirebaseUtil.getAuth().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        usersListener?.remove()
        updateUserStatus(false)
    }
}