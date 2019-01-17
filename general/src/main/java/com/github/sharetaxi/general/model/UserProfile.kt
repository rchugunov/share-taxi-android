package com.github.sharetaxi.general.model

data class UserProfile(
    val email: String? = null,
    val hexBytesPhotoPreview: String? = null,
    val photoPreviewUrl: String? = null,
    val firstName: String? = null,
    val secondName: String? = null
)
