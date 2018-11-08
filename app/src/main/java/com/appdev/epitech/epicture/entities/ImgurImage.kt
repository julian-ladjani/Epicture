package com.appdev.epitech.epicture.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImgurImage(
        val id: String = "id",
        val title: String = "id",
        val description: String = "id",
        val datetime: Long = 0,
        val type: String = "id",
        val animated: Boolean = false,
        val width: Int = 0,
        val height: Int = 0,
        val size: Int = 0,
        val views: String? = null,
        val bandwidth: Long = 0,
        val section: String? = "",
        val link: String? = "",
        val favorite: Boolean = false,
        val nsfw: String? = null,
        val vote: String? = "",
        val in_gallery: Boolean = false,
        val thumbnailLink: String? = ""
) : Parcelable