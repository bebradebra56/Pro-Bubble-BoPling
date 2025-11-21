package com.bubble.probubblebopling.ror.data

import java.util.*

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val amount: Double,
    val category: ExpenseCategory,
    val date: Date = Date(),
    val note: String = ""
)

enum class ExpenseCategory(val displayName: String) {
    FEED("Feed"),
    ELECTRICITY("Electricity"),
    MEDICINE("Medicine"),
    REPAIR("Repair"),
    OTHER("Other")
}