package com.bubble.probubblebopling.ror.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface EggBatchDao {
    @Query("SELECT * FROM eggbatch ORDER BY collectionDate DESC")
    fun getAllEggBatches(): Flow<List<EggBatch>>

    @Query("SELECT * FROM eggbatch WHERE collectionDate BETWEEN :start AND :end")
    fun getEggBatchesBetweenDates(start: Date, end: Date): Flow<List<EggBatch>>

    @Insert
    suspend fun insertEggBatch(eggBatch: EggBatch)

    @Delete
    suspend fun deleteEggBatch(eggBatch: EggBatch)

    @Query("SELECT SUM(quantity) FROM eggbatch WHERE collectionDate BETWEEN :start AND :end")
    fun getTotalEggsBetweenDates(start: Date, end: Date): Flow<Int>
}