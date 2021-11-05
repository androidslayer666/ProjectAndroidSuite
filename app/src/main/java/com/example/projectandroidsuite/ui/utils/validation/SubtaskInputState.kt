package com.example.projectandroidsuite.ui.utils.validation

import com.example.domain.utils.Result

data class SubtaskInputState(
    val isTitleEmpty: Boolean? = null,
    val isResponsibleEmpty: Boolean? = null,
    val serverResponse: Result<String, String>? = null
)