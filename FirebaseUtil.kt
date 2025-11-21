package com.example.muconnect.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object FirebaseUtil {
    fun getAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    fun getFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    fun getCurrentUserId(): String = getAuth().currentUser?.uid ?: ""

    fun getChatId(userId1: String, userId2: String): String {
        return if (userId1 < userId2) "${userId1}_${userId2}" else "${userId2}_${userId1}"
    }
}