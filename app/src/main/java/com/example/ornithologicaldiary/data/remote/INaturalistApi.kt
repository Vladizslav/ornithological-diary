package com.example.ornithologicaldiary.data.remote

import com.example.ornithologicaldiary.data.remote.dto.TaxaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface INaturalistApi {

    @GET("v1/taxa")
    suspend fun searchBirds(
        @Query("q") query: String,
        @Query("taxon_id") taxonId: Int,
        @Query("locale") locale: String,
        @Query("per_page") perPage: Int
    ): TaxaResponse
}
