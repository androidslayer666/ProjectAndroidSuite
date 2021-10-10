package com.example.domain.mappers

import com.example.database.entities.UserEntity
import com.example.domain.model.User
import com.example.network.dto.UserDto


fun UserDto.toUserEntity(): UserEntity {
    return UserEntity(
        id =this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        displayName = this.displayName ?: this.firstName + this.lastName,
        email = this.email,
        avatarSmall = this.avatarSmall,
        profileUrl = this.profileUrl,
        avatarMedium = this.avatarMedium
    )
}


fun List<UserDto>.toListUserEntity(): MutableList<UserEntity> {
    val listUserEntity = mutableListOf<UserEntity>()
    for (user in this) {
        listUserEntity.add(user.toUserEntity())
    }
    return listUserEntity
}

fun List<UserEntity>.fromListUserEntitiesToListUsers() : List<User> {
    val list = mutableListOf<User>()
    list.addAll(this.map { it.fromUserEntityToUser() })
    return list
}


fun UserEntity.fromUserEntityToUser(): User {
    return User(
        id =this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        displayName = this.displayName,
        email = this.email,
        avatarSmall = this.avatarSmall,
        profileUrl = this.profileUrl,
        avatarMedium = this.avatarMedium
    )
}
