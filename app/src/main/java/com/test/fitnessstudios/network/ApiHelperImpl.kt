package com.test.fitnessstudios.network

import com.test.fitnessstudios.data.DirectionsResponse
import com.test.fitnessstudios.data.SearchBusinessResponse
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun searchBusiness(
        lat: Double, long: Double, category: String, radius: Int, sortBy: String
    ): Response<SearchBusinessResponse> {
        return apiService.searchBusiness(
            lat, long, category, radius, sortBy
        )
    }

    override suspend fun getDirections(url: String): Response<DirectionsResponse> {
        return apiService.getDirections(
            url
        )
    }
}