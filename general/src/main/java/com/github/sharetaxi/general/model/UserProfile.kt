package com.github.sharetaxi.general.model

import com.google.gson.annotations.SerializedName

data class UserProfile(
    val email: String? = null,
    @SerializedName("photo_preview_hex") val hexBytesPhotoPreview: String? = null,
    @SerializedName("photo_url") val photoUrl: String? = null,
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("last_name") val secondName: String? = null
)
