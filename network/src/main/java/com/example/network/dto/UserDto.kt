package com.example.network.dto

import com.google.gson.annotations.SerializedName

data class UserDto (
    @SerializedName("id")
    var id: String,
    @SerializedName("first_name")
    var firstName: String?=null,
    @SerializedName("last_name")
    var lastName: String?=null,
    @SerializedName("displayName")
    val displayName : String? = firstName+lastName,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("avatarSmall")
    val avatarSmall : String? = null,
    @SerializedName("avatarMedium")
    val avatarMedium : String? = null,
    @SerializedName("profileUrl")
    val profileUrl: String? = null,
    @SerializedName("isVisitor")
    val isVisitor: Boolean? = null
)
