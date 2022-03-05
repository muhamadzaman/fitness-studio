package com.test.fitnessstudios.network

import com.test.fitnessstudios.data.DirectionsResponse
import com.test.fitnessstudios.data.SearchBusinessResponse
import retrofit2.Response

interface ApiHelper {
    suspend fun searchBusiness(
        lat: Double,
        long: Double,
        category: String,
        radius: Int,
        sortBy: String
    ): Response<SearchBusinessResponse>

    suspend fun getDirections(url: String) : Response<DirectionsResponse>
}