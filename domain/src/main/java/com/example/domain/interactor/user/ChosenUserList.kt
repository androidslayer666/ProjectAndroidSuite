package com.example.domain.interactor.user

import com.example.domain.model.User

interface ChosenUserList {
    operator fun invoke(userList: List<User>)
    }