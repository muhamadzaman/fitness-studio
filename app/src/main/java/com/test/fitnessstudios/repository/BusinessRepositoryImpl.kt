package com.test.fitnessstudios.repository

import com.test.fitnessstudios.data.SearchBusinessResponse
import com.test.fitnessstudios.network.ApiHelper
import javax.inject.Inject

class BusinessRepositoryImpl @Inject constructor(
    private val apiHelper: ApiHelper
): BusinessRepository {
    override suspend fun searchBusiness(
        lat: Double,
        long: Double,
        category: String,
        radius: Int,
        sortBy: String
    ) :Resource<SearchBusinessResponse> {
        //TODO improve error message handling
        val response = apiHelper.searchBusiness(lat, long, category, radius, sortBy)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        } else {
            return Resource.Error("Something Went Wrong")

        }
        return Resource.Error("Something Went Wrong")
    }
}