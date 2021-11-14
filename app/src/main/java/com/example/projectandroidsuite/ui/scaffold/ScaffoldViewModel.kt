package com.example.projectandroidsuite.ui.scaffold

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.login.GetSelfProfile
import com.example.domain.interactor.login.Logout
import com.example.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScaffoldViewModel @Inject constructor(
    private val logout: Logout,
    private val getSelfProfile: GetSelfProfile
) :ViewModel(){

    private var _self = MutableStateFlow<User?>(null)
    val self: StateFlow<User?> = _self

    init{
        getSelf()
    }

    private fun getSelf() {
        viewModelScope.launch {
            getSelfProfile().collectLatest {
                _self.value = it
            }
        }
    }

    fun logOut(){
        logout()
    }


}