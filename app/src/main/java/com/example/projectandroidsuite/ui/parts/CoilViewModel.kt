package com.example.projectandroidsuite.ui.parts

import androidx.lifecycle.ViewModel
import com.example.domain.AuthCredentialsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoilViewModel @Inject constructor(
    private val authCredentialsProvider: AuthCredentialsProvider
) : ViewModel() {

    fun fetchPortalAddress(): String? {
        return authCredentialsProvider.fetchPortalAddress()
    }

    fun fetchToken(): String? {
        return authCredentialsProvider.fetchAuthToken()
    }

}