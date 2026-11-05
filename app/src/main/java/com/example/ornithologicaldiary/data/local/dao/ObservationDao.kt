package com.example.ornithologicaldiary.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ornithologicaldiary.data.local.ObservationWithSpecies
import com.example.ornithologicaldiary.data.local.entity.ObservationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ObservationDao {

    @Query(
        """
        SELECT o.*, s.name AS speciesName
        FROM observations o
        INNER JOIN species s ON s.id = o.speciesId
        ORDER BY o.observedAt DESC
        """
    )
    fun observeAll(): Flow<List<ObservationWithSpecies>>

    @Query("SELECT * FROM observations WHERE id = :id")
    suspend fun findById(id: Long): ObservationEntity?

    @Query("SELECT * FROM observations WHERE speciesId = :speciesId")
    suspend fun getBySpeciesId(speciesId: Long): List<ObservationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(observation: ObservationEntity): Long

    @Update
    suspend fun update(observation: ObservationEntity)

    @Query("DELETE FROM observations WHERE id = :id")
    suspend fun deleteById(id: Long)
}
