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

    private var _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init{
        //getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            getSelfProfile().collectLatest {
                _user.value = it
            }
        }
    }

    fun logOut(){
        logout()
    }


}