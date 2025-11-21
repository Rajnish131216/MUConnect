package com.example.muconnect.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String = "",
    val studentId: String = "",
    val email: String = "",
    val name: String = "",
    val department: String = "",
    val year: String = "",
    val profileImageUrl: String = "",
    val isOnline: Boolean = false,
    val lastSeen: Long = System.currentTimeMillis(),
    val fcmToken: String = ""
) : Parcelable