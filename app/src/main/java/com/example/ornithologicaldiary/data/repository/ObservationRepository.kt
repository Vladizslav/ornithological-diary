package com.example.ornithologicaldiary.data.repository

import com.example.ornithologicaldiary.data.local.AppDatabase
import com.example.ornithologicaldiary.data.local.ObservationWithSpecies
import com.example.ornithologicaldiary.data.local.entity.ObservationEntity
import kotlinx.coroutines.flow.Flow

class ObservationRepository(database: AppDatabase) {

    private val dao = database.observationDao()

    fun observeAll(): Flow<List<ObservationWithSpecies>> = dao.observeAll()

    suspend fun findById(id: Long): ObservationEntity? = dao.findById(id)

    suspend fun save(observation: ObservationEntity): Long =
        if (observation.id == 0L) dao.insert(observation) else {
            dao.update(observation)
            observation.id
        }

    suspend fun deleteById(id: Long) = dao.deleteById(id)
}
