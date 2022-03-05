package com.test.fitnessstudios.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    @Json(name = "address1")
    val address1 :String?,
    @Json(name = "address2")
    val address2 :String?,
    @Json(name = "address3")
    val address3 :String?,
    @Json(name = "city")
    val city :String?,
    @Json(name = "zip_code")
    val zipCode :String?,
    @Json(name = "country")
    val country :String?,
    @Json(name = "state")
    val state :String?,
    @Json(name = "display_address")
    val display_address :List<String>?,
) : Parcelable
