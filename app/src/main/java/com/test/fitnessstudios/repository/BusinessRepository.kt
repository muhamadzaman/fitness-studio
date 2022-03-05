package com.test.fitnessstudios.repository

import com.test.fitnessstudios.data.SearchBusinessResponse

interface BusinessRepository {
    suspend fun searchBusiness(
        lat: Double,
        long: Double,
        category: String,
        radius: Int,
        sortBy: String
    ) :Resource<SearchBusinessResponse>
}