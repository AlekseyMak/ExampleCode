package com.exampleproject.network

import androidx.room.TypeConverter

class FriendshipStatusConverter {

    @TypeConverter
    fun intToFriendshipStatus(data: Int): FriendshipStatus {
        return FriendshipStatus.values().firstOrNull { it.rawValue == data } ?: FriendshipStatus.unknown
    }

    @TypeConverter
    fun FriendshipStatusToInt(type: FriendshipStatus): Int {
        return type.rawValue
    }
}