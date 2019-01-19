package com.github.sharetaxi.general.web.response

import com.github.sharetaxi.general.model.UserProfile
import com.google.gson.annotations.SerializedName

class UserResponse(@SerializedName("user") val userProfile: UserProfile) : BaseResponse()