package com.test.fitnessstudios.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    @Json(name = "alias")
    val alias: String?,
    @Json(name = "title")
    val title: String?,
) : Parcelable
