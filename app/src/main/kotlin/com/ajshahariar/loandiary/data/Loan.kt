package com.ajshahariar.loandiary.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loans")
data class Loan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val personName: String,
    val amount: Double,
    val type: String, // "LENT" or "BORROWED"
    val currency: String = "USD", // "USD" or "BDT"
    val date: Long = System.currentTimeMillis(),
    val dueDate: Long = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000L), // 7 days default
    val isRepaid: Boolean = false,
    val note: String = "",
    val repaymentsJson: String = "[]" // Format: [{"amount":50.0,"date":1710000000000}]
)
