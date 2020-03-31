package com.exampleproject

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AvatarConverter {
    private val gson = Gson()

    @TypeConverter
    fun stringToAvatar(data: String): AvatarData? {
        if (data.isBlank()) {
            return null
        }

        val listType = object : TypeToken<AvatarData?>() {

        }.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun avatarToString(avatar: AvatarData?): String {
        return if (avatar == null) {
            ""
        } else {
            gson.toJson(avatar)
        }
    }
}