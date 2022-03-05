package com.test.fitnessstudios.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coordinates(
    @Json(name = "latitude")
    val latitude : Double = 0.0,
    @Json(name = "longitude")
    val longitude : Double = 0.0,
) : Parcelable
