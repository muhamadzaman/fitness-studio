package com.test.fitnessstudios.repository

import com.test.fitnessstudios.data.DirectionsResponse

interface AppRepository {
    suspend fun getDirections(
        url: String
    ) :Resource<DirectionsResponse>
}