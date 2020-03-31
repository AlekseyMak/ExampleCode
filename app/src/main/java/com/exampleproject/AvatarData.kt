package com.exampleproject

import com.google.gson.annotations.SerializedName
import java.net.URL

enum class ImageSizeType {
    small,
    medium,
    original;

    fun url(suffix: String?): URL? {
        if (!suffix.isNullOrEmpty()) {
            return URL("https://n.xxxx.com/${suffix}")
        }
        return null
    }
}

data class AvatarData(
    @SerializedName("m")
    val medium: String?,
    @SerializedName("s")
    val small: String?,
    @SerializedName("o")
    val original: String?
) {


    fun url(type: ImageSizeType): URL? {
        val suffix: String? = when (type) {
            ImageSizeType.small -> small
            ImageSizeType.medium -> medium
            ImageSizeType.original -> original
            else -> null
        }
        return type.url(suffix)
    }
}
