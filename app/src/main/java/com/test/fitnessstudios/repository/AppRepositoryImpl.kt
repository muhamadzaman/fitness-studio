package com.test.fitnessstudios.repository

import android.util.Log
import com.test.fitnessstudios.data.DirectionsResponse
import com.test.fitnessstudios.network.ApiHelper
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val apiHelper: ApiHelper
): AppRepository {
    override suspend fun getDirections(
        url : String
    ) :Resource<DirectionsResponse> {
        //TODO improve error message handling
        val response = apiHelper.getDirections(url)
        if (response.isSuccessful) {
            response.body()?.let {
                Log.d("Usman", "searchBusiness: $it")
                return Resource.Success(it)
            }
        } else {
            Log.d("Usman", "searchBusiness: error")
            return Resource.Error("Something Went Wrong")
        }
        Log.d("Usman", "searchBusiness: default error")
        return Resource.Error("Something Went Wrong")
    }
}