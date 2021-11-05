package com.example.projectandroidsuite.ui.utils.validation

import com.example.domain.utils.Result

data class LoginInputState (
        val isPortalValidated: Boolean? = null,
        val isEmailValidated: Boolean? = null,
        val isPasswordValidated: Boolean? = null,
        val isCodeValidated: Boolean? = true,
        val canConnectToPortal: Boolean? = null,
        val twoFactorAuth: Boolean? = null,
        val serverResponseError: String? = null
    ) {
        fun readyToTryAuth(): Boolean {
                return isPortalValidated == true
                        && isEmailValidated == true && isPasswordValidated == true && canConnectToPortal == true
        }
}