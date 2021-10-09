package com.example.projectandroidsuite.ui.scaffold

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.database.entities.UserEntity
import com.example.domain.repository.AuthRepositoryImpl
import com.example.domain.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ScaffoldViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val authRepository: AuthRepositoryImpl
) :ViewModel(){

    private var _self = MutableLiveData<UserEntity>()
    val self: LiveData<UserEntity> = _self

    init{
        getSelf()
    }

    fun getSelf() {
        viewModelScope.launch {
            val self = teamRepository.getSelfProfile()
            //Log.d("ScaffoldViewModel", self.toString())
            withContext(Main){
                if(self != null)
                _self.value = self!!
            }
        }
    }

    fun logOut(){
        authRepository.logOut()
    }


}