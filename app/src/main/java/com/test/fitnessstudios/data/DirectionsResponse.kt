package com.test.fitnessstudios.data

import com.squareup.moshi.Json



data class DirectionsResponse(
    @Json(name = "routes")
    val routes : List<Route>
)

data class Route(
    @field:Json(name = "overview_polyline")
    val overviewPolyline : OverviewPolyline
)

data class OverviewPolyline(
    @Json(name = "points")
    val points : String?
)
