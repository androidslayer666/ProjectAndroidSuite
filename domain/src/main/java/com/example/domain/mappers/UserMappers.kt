package com.example.domain.mappers

import com.example.database.entities.UserEntity
import com.example.network.dto.UserDto


fun UserDto.toUserEntity(): UserEntity {
    return UserEntity(
        this.id,
        this.firstName,
        this.lastName,
        this.displayName ?: this.firstName + this.lastName,
        this.email,
        this.avatarSmall,
        this.profileUrl
    )
}


fun List<UserDto>.toListUserEntity(): MutableList<UserEntity> {
    val listUserEntity = mutableListOf<UserEntity>()
    for (user in this) {
        listUserEntity.add(user.toUserEntity())
    }
    return listUserEntity
}


