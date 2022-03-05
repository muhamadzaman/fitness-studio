package com.test.fitnessstudios.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Business(
    @Json(name = "id")
    val id: String,
    @Json(name="alias")
    val alias: String?,
    @Json(name="name")
    val name: String?,
    @field:Json(name="image_url")
    val imageUrl: String?,
    @field:Json(name="is_closed")
    val isClosed: Boolean = false,
    @Json(name="url")
    val url: String?,
    @field:Json(name="review_count")
    val reviewCount: Int?,
    @Json(name="categories")
    val categories: List<Category>?,
    @Json(name="rating")
    val rating: Double?,
    @Json(name="coordinates")
    val coordinates: Coordinates?,
    @Json(name="location")
    val location: Location,
    @Json(name="phone")
    val phone: String,
    @field:Json(name="display_phone")
    val displayPhone: String?,
    @Json(name="distance")
    val distance: Double?,
    ) : Parcelable
