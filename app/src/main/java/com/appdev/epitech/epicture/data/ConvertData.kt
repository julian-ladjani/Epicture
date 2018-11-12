package com.appdev.epitech.epicture.data

import com.appdev.epitech.epicture.entities.*
import com.github.kittinunf.forge.core.JSON
import com.google.gson.Gson
import org.json.JSONObject

class ConvertData {
    companion object {

        val thumbnailMode = "m"

        private fun getJsonData(jsonResponse: String): String {
            val responseObject = JSONObject(jsonResponse)
            if (responseObject.getBoolean("success")) {
                return responseObject.get("data").toString()
            } else {
                // Horrible hack, fixme
                return "[]"
            }
        }

        fun stringtoAccount(data: String?): Account{
            val gson = Gson()
            val imgurJson = getJsonData(data.toString())
            var imgurAccount = gson.fromJson(imgurJson, ImgurAccount::class.java)
            var bioAccount = ""

            if (imgurAccount != null) {
                bioAccount = imgurAccount.bio
            }
            return Account(imgurAccount.url,
                    bioAccount,
                    imgurAccount.created,
                    imgurAccount.reputation
            )
        }

        fun makeImgurImage(image: ImgurImage, titleAlbum:String, listImage: MutableList<ImgurImage>){
            var title = ""
            if (image.title == null) {
                title = titleAlbum
            } else {
                title = image.title
            }
            var description = ""
            if (image.description != null) {
                description = image.description
            }
            var thumbnailLink = image.link
            if (image.link!!.substringAfterLast(".") != "gif")
                thumbnailLink = image.link!!.substring(0, (image.link.length) - 4) + "${thumbnailMode}." + image.link.substringAfterLast(".")
            val imgurImage = ImgurImage(
                    image.id,
                    title,
                    description,
                    image.datetime,
                    image.type,
                    image.animated,
                    image.width,
                    image.height,
                    image.size,
                    image.views,
                    image.bandwidth,
                    image.section,
                    image.link,
                    image.favorite,
                    image.nsfw,
                    image.vote,
                    image.in_gallery,
                    thumbnailLink

            )
            listImage.add(imgurImage)
        }

        fun imagesToMutableListImgurImage(data: String?, listImage: MutableList<ImgurImage>):MutableList<ImgurImage> {
            val gson = Gson()
            val imgurGallery = getJsonData(data.toString())
            val images = gson.fromJson(imgurGallery, Array<ImgurImage>::class.java)
            for (image in images)
                if (!image.in_gallery)
                    makeImgurImage(image, "", listImage)
            return listImage
        }

        fun galleryToMutableListImgurImage(data: String?, listImage: MutableList<ImgurImage>): MutableList<ImgurImage> {
            val gson = Gson()
            val imgurGallery = getJsonData(data.toString())
            val gallery = gson.fromJson(imgurGallery, Array<ImgurGalleryAlbum>::class.java)
            for (album in gallery)
                if (album.images_count > 0)
                    for (image in album.images)
                        makeImgurImage(image, album.title, listImage)
            return listImage
        }
    }
}