package com.example.ornithologicaldiary.data.repository

import com.example.ornithologicaldiary.data.remote.INaturalistApi
import com.example.ornithologicaldiary.data.remote.NetworkModule
import com.example.ornithologicaldiary.data.remote.dto.TaxonDto

class BirdApiRepository(
    private val api: INaturalistApi = NetworkModule.birdApi
) {

    suspend fun searchBirds(query: String): List<TaxonDto> =
        api.searchBirds(
            query = query,
            taxonId = BIRDS_TAXON_ID,
            locale = LOCALE,
            perPage = PAGE_SIZE
        ).results
            .filter { it.rank == SPECIES_RANK }
            .filter { !it.commonName.isNullOrBlank() }

    companion object {
        private const val BIRDS_TAXON_ID = 3
        private const val LOCALE = "ru"
        private const val PAGE_SIZE = 30
        private const val SPECIES_RANK = "species"
    }
}
