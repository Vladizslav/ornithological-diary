package com.example.ornithologicaldiary.data.repository

import com.example.ornithologicaldiary.data.local.AppDatabase
import com.example.ornithologicaldiary.data.local.entity.SpeciesEntity
import kotlinx.coroutines.flow.Flow

class SpeciesRepository(database: AppDatabase) {

    private val dao = database.speciesDao()

    fun observeAll(): Flow<List<SpeciesEntity>> = dao.observeAll()

    suspend fun getAll(): List<SpeciesEntity> = dao.getAll()

    suspend fun findById(id: Long): SpeciesEntity? = dao.findById(id)

    suspend fun findByName(name: String): SpeciesEntity? = dao.findByName(name)

    suspend fun getOrCreate(name: String): SpeciesEntity {
        dao.findByName(name)?.let { return it }
        val id = dao.insert(SpeciesEntity(name = name, latinName = ""))
        return SpeciesEntity(id = id, name = name, latinName = "")
    }

    suspend fun save(species: SpeciesEntity): Long =
        if (species.id == 0L) dao.insert(species) else {
            dao.update(species)
            species.id
        }

    suspend fun delete(species: SpeciesEntity) = dao.delete(species)
}
