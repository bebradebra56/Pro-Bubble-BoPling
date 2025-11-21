package com.bubble.probubblebopling.ror.data

import kotlinx.coroutines.flow.Flow
import java.util.Date

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    fun getAllExpenses(): Flow<List<Expense>> = expenseDao.getAllExpenses()

    suspend fun insertExpense(expense: Expense) = expenseDao.insertExpense(expense)

    suspend fun deleteExpense(expense: Expense) = expenseDao.deleteExpense(expense)

    fun getExpensesBetweenDates(start: Date, end: Date): Flow<List<Expense>> =
        expenseDao.getExpensesBetweenDates(start, end)

    fun getTotalExpensesBetweenDates(start: Date, end: Date): Flow<Double> =
        expenseDao.getTotalExpensesBetweenDates(start, end)
}