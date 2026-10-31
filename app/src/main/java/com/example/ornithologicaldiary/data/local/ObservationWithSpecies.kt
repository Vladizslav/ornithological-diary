package com.example.ornithologicaldiary.data.local

import androidx.room.Embedded
import com.example.ornithologicaldiary.data.local.entity.ObservationEntity

data class ObservationWithSpecies(
    @Embedded val observation: ObservationEntity,
    val speciesName: String
)
