package com.bubble.probubblebopling.ror.data

import java.util.*

data class EggBatch(
    val id: String = UUID.randomUUID().toString(),
    val quantity: Int,
    val collectionDate: Date = Date()
) {
    val expirationDate: Date
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = collectionDate
            calendar.add(Calendar.DAY_OF_MONTH, 21)
            return calendar.time
        }

    val freshness: FreshnessStatus
        get() {
            val today = Date()
            val diffInMillis = expirationDate.time - today.time
            val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)

            return when {
                diffInDays < 0 -> FreshnessStatus.EXPIRED
                diffInDays <= 2 -> FreshnessStatus.SOON_EXPIRING
                else -> FreshnessStatus.FRESH
            }
        }
}

enum class FreshnessStatus {
    FRESH, SOON_EXPIRING, EXPIRED
}