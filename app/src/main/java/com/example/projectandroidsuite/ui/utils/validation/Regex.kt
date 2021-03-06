package com.example.projectandroidsuite.ui.utils.validation


fun validatePortalNameInput(input: String): Boolean {
    val regex = Regex(
        pattern = "([a-zA-Z]+(\\.onlyoffice+)+(\\.com|.eu|.sg)+)\$",
        options = setOf(RegexOption.IGNORE_CASE)
    )
    return regex.matches(input)
}

fun validateEmail(input: String): Boolean {
    val regex = Regex(
        pattern = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+))",
        options = setOf(RegexOption.IGNORE_CASE)
    )
    return regex.matches(input)
}

fun validatePassword(input: String): Boolean {
    val regex = Regex(
        pattern = "(?=^.{8,}\$).*\$",
        options = setOf(RegexOption.IGNORE_CASE)
    )
    return regex.matches(input)
}

fun validateCode(input: String): Boolean {
    val regex = Regex(
        pattern = "^[0-9]*$"
    )
    return regex.matches(input)
}

