package com.exampleproject.network

import com.google.gson.annotations.SerializedName

enum class DeletionState(val rawValue: Int) {
    @SerializedName("0")
    none(0),
    @SerializedName("1")
    deletedByContact(1),
    @SerializedName("2")
    deletedByMe(2),
    @SerializedName("3")
    blockedByContact(3),
    @SerializedName("4")
    blockedByMe(4),
    @SerializedName("5")
    unknown(5);

    companion object {
        operator fun invoke(rawValue: Int) =
            DeletionState.values().firstOrNull { it.rawValue == rawValue }
    }
}

public enum class FriendshipStatus(val rawValue: Int) {
    @SerializedName("-1")
    requestedByMe(-1),
    @SerializedName("0")
    requestedByContact(0),
    @SerializedName("1")
    friends(1),
    @SerializedName("2")
    unknown(2);

    companion object {
        operator fun invoke(rawValue: Int) =
            FriendshipStatus.values().firstOrNull { it.rawValue == rawValue }
    }
}
