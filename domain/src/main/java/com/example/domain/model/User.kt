package com.example.domain.model


data class User (
    var id: String,
    var firstName: String? = "",
    var lastName: String? = "",
    val displayName : String = firstName+lastName,
    var email: String? = null,
    val avatarSmall : String? = null,
    val avatarMedium : String? = null,
    val profileUrl: String? = null,
    var chosen: Boolean? = null,
    val self: Boolean = false
)
