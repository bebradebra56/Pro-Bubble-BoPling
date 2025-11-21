package com.bubble.probubblebopling.ror.data

import kotlinx.coroutines.flow.Flow
import java.util.Date

class EggBatchRepository(private val eggBatchDao: EggBatchDao) {
    fun getAllEggBatches(): Flow<List<EggBatch>> = eggBatchDao.getAllEggBatches()

    suspend fun insertEggBatch(eggBatch: EggBatch) = eggBatchDao.insertEggBatch(eggBatch)

    suspend fun deleteEggBatch(eggBatch: EggBatch) = eggBatchDao.deleteEggBatch(eggBatch)

    fun getEggBatchesBetweenDates(start: Date, end: Date): Flow<List<EggBatch>> =
        eggBatchDao.getEggBatchesBetweenDates(start, end)

    fun getTotalEggsBetweenDates(start: Date, end: Date): Flow<Int> =
        eggBatchDao.getTotalEggsBetweenDates(start, end)
}