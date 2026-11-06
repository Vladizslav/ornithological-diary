package com.example.ornithologicaldiary.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ornithologicaldiary.data.local.entity.SpeciesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeciesDao {

    @Query("SELECT * FROM species ORDER BY name")
    fun observeAll(): Flow<List<SpeciesEntity>>

    @Query("SELECT * FROM species ORDER BY name")
    suspend fun getAll(): List<SpeciesEntity>

    @Query("SELECT * FROM species WHERE id = :id")
    suspend fun findById(id: Long): SpeciesEntity?

    @Query("SELECT * FROM species WHERE name = :name COLLATE NOCASE LIMIT 1")
    suspend fun findByName(name: String): SpeciesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(species: SpeciesEntity): Long

    @Update
    suspend fun update(species: SpeciesEntity)

    @Delete
    suspend fun delete(species: SpeciesEntity)
}
