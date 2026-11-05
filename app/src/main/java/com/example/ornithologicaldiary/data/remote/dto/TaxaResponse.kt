package com.example.ornithologicaldiary.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TaxaResponse(
    @SerializedName("results") val results: List<TaxonDto> = emptyList()
)

data class TaxonDto(
    @SerializedName("id") val id: Long = 0L,
    @SerializedName("name") val latinName: String? = null,
    @SerializedName("preferred_common_name") val commonName: String? = null,
    @SerializedName("rank") val rank: String? = null,
    @SerializedName("observations_count") val observationsCount: Int = 0
)
