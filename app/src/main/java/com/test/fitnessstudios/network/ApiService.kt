package com.test.fitnessstudios.network

import com.test.fitnessstudios.data.DirectionsResponse
import com.test.fitnessstudios.data.SearchBusinessResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET("businesses/search")
    suspend fun searchBusiness(
        @Query("latitude") lat : Double ,
        @Query("longitude") long: Double,
        @Query("categories") category: String ,
        @Query("radius") radius : Int ,
        @Query("sort_by") sortBy : String) : Response<SearchBusinessResponse>

    @GET
    suspend fun getDirections(
        @Url url : String)
    : Response<DirectionsResponse>
}