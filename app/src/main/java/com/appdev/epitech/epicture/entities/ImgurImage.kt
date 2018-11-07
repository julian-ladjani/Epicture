package com.appdev.epitech.epicture.entities

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
        val views: Int = 0,
        val bandwidth: Long = 0,
        val section: String = "",
        val link: String = "",
        val favorite: Boolean = false,
        val nsfw: Boolean = false,
        val vote: String = "",
        val in_gallery: Boolean = false,
        val thumbnailLink: String = ""
)