package com.example.projectandroidsuite.ui.utils.validation

import com.example.domain.utils.Result

data class TaskInputState (
    val isTitleEmpty: Boolean? = null,
    val isProjectEmpty: Boolean? = null,
    val isTeamEmpty: Boolean? = null,
    val serverResponse: Result<String, String>? = null
)