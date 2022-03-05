package com.test.fitnessstudios.data

import com.squareup.moshi.Json

data class SearchBusinessResponse(
    @Json(name = "businesses")
    val businesses : List<Business>?
)