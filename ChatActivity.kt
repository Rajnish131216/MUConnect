package com.example.muconnect

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muconnect.adapters.MessageAdapter
import com.example.muconnect.databinding.ActivityChatBinding
import com.example.muconnect.models.Message
import com.example.muconnect.models.User
import com.example.muconnect.utils.FirebaseUtil
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private val messagesList = mutableListOf()
    private lateinit var receiverUser: User
    private var messagesListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiverUser = intent.getParcelableExtra("user") ?: return

        setupToolbar()
        setupRecyclerView()
        loadMessages()

        binding.btnSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = receiverUser.name
            subtitle = if (receiverUser.isOnline) "Online" else "Offline"
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(messagesList, FirebaseUtil.getCurrentUserId())

        binding.recyclerViewMessages.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = true
            }
            adapter = messageAdapter
        }
    }

    private fun loadMessages() {
        val currentUserId = FirebaseUtil.getCurrentUserId()
        val chatId = FirebaseUtil.getChatId(currentUserId, receiverUser.uid)

        messagesListener = FirebaseUtil.getFirestore()
            .collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                messagesList.clear()
                snapshot?.documents?.forEach { doc ->
                    val message = doc.toObject(Message::class.java)
                    message?.let { messagesList.add(it) }
                }
                messageAdapter.notifyDataSetChanged()

                if (messagesList.isNotEmpty()) {
                    binding.recyclerViewMessages.scrollToPosition(messagesList.size - 1)
                }
            }
    }

    private fun sendMessage() {
        val messageText = binding.etMessage.text.toString().trim()
        if (messageText.isEmpty()) return

        val currentUserId = FirebaseUtil.getCurrentUserId()
        val chatId = FirebaseUtil.getChatId(currentUserId, receiverUser.uid)
        val messageId = FirebaseUtil.getFirestore().collection("chats").document().id

        val message = Message(
            messageId = messageId,
            senderId = currentUserId,
            receiverId = receiverUser.uid,
            message = messageText,
            timestamp = System.currentTimeMillis()
        )

        FirebaseUtil.getFirestore()
            .collection("chats")
            .document(chatId)
            .collection("messages")
            .document(messageId)
            .set(message)
            .addOnSuccessListener {
                binding.etMessage.text?.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to send message: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        messagesListener?.remove()
    }
}