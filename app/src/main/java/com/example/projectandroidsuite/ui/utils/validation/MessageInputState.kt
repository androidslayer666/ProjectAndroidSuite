package com.example.projectandroidsuite.ui.utils.validation

import com.example.domain.utils.Result

data class MessageInputState(
    val isTitleEmpty: Boolean? = null,
    val isTextEmpty: Boolean? = null,
    val isTeamEmpty: Boolean? = null,
    val serverResponse: Result<String, String>? = null
)