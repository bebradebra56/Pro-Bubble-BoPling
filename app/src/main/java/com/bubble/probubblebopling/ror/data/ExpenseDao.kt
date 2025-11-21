package com.bubble.probubblebopling.ror.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expense ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expense WHERE date BETWEEN :start AND :end ORDER BY date DESC")
    fun getExpensesBetweenDates(start: Date, end: Date): Flow<List<Expense>>

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT SUM(amount) FROM expense WHERE date BETWEEN :start AND :end")
    fun getTotalExpensesBetweenDates(start: Date, end: Date): Flow<Double>
}