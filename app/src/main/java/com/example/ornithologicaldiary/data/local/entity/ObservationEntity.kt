package com.example.ornithologicaldiary.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "observations",
    foreignKeys = [
        ForeignKey(
            entity = SpeciesEntity::class,
            parentColumns = ["id"],
            childColumns = ["speciesId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("speciesId")]
)
data class ObservationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val speciesId: Long,
    val place: String,
    val observedAt: Long,
    val note: String,
    val photoPath: String? = null
)
