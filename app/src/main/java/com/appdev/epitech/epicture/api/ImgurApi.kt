package com.appdev.epitech.epicture.api

import android.content.Context
import android.util.Base64
import com.google.gson.Gson
import com.appdev.epitech.epicture.SecretUtils
import com.appdev.epitech.epicture.data.ConvertData
import com.appdev.epitech.epicture.entities.*
import com.appdev.epitech.epicture.GridActivity
import com.appdev.epitech.epicture.ImageActivity
import com.appdev.epitech.epicture.adapters.ImageGridAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.File

class ImgurApi {
    companion object {
        val clientId = "571a8127eb51724"
        val thumbnailMode = "b"
        private fun getJsonData(jsonResponse: String): String {
            val responseObject = JSONObject(jsonResponse)
            if (responseObject.getBoolean("success")) {
                return responseObject.get("data").toString()
            } else {
                // Horrible hack, fixme
                return "[]"
            }
        }

        fun getSelfAccount(context: Context): Account? {
            val (isFull, result) = SecretUtils.getSecrets(context)
            var account: Account? = null
            "/3/account/me".httpGet().responseString { request, response, result ->
                //make a GET to https://httpbin.org/get and do something with response
                val (data, error) = result
                if (error != null) {
                    println(error)
                } else {
                    account = ConvertData.stringtoAccount(data)
                }
            }
            return account
        }

        fun getGallery(context: Context, section: Int, sort: Int, page: Int, nsfwEnabled: Boolean): MutableList<ImgurImage> {
            val sectionParam = when (section) {
                0 -> "hot"
                1 -> "top"
                2 -> "user"
                else -> "hot"
            }

            val sortParam = when (sort) {
                0 -> "viral"
                1 -> "time"
                else -> "top" // Top (x)
            }

            val timeWindow = when (sort) {
                2 -> "day"
                3 -> "week"
                4 -> "month"
                5 -> "year"
                6 -> "all"
                else -> ""
            }

            var url = "/gallery/$sectionParam/$sortParam/$page"
            if (!timeWindow.equals("")) {
                url += "$timeWindow/"
            }

            url += "?mature=$nsfwEnabled&album_previews=true"
            println("URL:$url")
            var listImage = mutableListOf<ImgurImage>()
            url.httpGet()
                    .responseString { request, response, result ->
                        //make a GET to https://httpbin.org/get and do something with response
                        val (data, error) = result
                        if (error != null)
                            println("ERROR $error")
                        else {
                            listImage = ConvertData.galleryToMutableListImgurImage(data, listImage)
                            val activity = context as GridActivity
                            activity.loadGrid(listImage)
                        }
                    }
            return listImage
        }

        fun getSubredditGallery(subreddit: String): Array<SubredditImage> {
            val url = "https://api.imgur.com/3/gallery/r/$subreddit/top/week/"
            val request = HttpUtils.createRequest(url, mapOf("Authorization" to "Client-ID ${clientId}"))
            val response = HttpUtils.sendRequest(request)
            val body = response.body()!!
            val jsonResponse = body.string()
            val imgurJson = getJsonData(jsonResponse)
            val gson = Gson()
            val subredditGallery = gson.fromJson(imgurJson, Array<ImgurGalleryAlbum>::class.java)
            val subredditImages = subredditGallery.map {
                SubredditImage(
                        if (it.is_album) {
                            it.cover
                        } else {
                            it.id
                        },
                        it.title,
                        it.datetime
                )
            }

            return subredditImages.toTypedArray()
        }

        fun getComments(image: ImgurImage): Array<Comment> {
            val id = image.id
            val url = "https://api.imgur.com/3/gallery/$id/comments/best"
            val request = HttpUtils.createRequest(url, mapOf("Authorization" to "Client-ID ${clientId}"))
            val response = HttpUtils.sendRequest(request)
            val body = response.body()!!
            val jsonResponse = body.string()
            val imgurJson = getJsonData(jsonResponse)
            val gson = Gson()
            val imgurComments = gson.fromJson(imgurJson, Array<ImgurComment>::class.java)
            val comments = imgurComments.map {
                Comment(
                        it.id,
                        it.comment,
                        it.datetime,
                        it.points,
                        it.author
                )
            }

            return comments.toTypedArray()
        }

        fun uploadImage(file: ByteArray) {
            println(file.toString())
            val base64Encoded = Base64.encodeToString(file, Base64.DEFAULT)
            "/image".httpPost(listOf(Pair("image", base64Encoded)))
                    .response { request, response, result ->
                        val (data, error) = result
                        if (error != null)
                            println("ERROR $error")
                        println("Data:$data")
                    }
        }

        fun getSearch(context: Context, search: ArrayList<ParameterSearch>, sort: Int, page: Int): MutableList<ImgurImage> {
            val sortParam = when (sort) {
                0 -> "viral"
                1 -> "time"
                else -> "top" // Top (x)
            }

            val timeWindow = when (sort) {
                2 -> "day"
                3 -> "week"
                4 -> "month"
                5 -> "year"
                6 -> "all"
                else -> ""
            }

            var url = "gallery/search/$sortParam/$page"
            if (!timeWindow.equals("")) {
                url += timeWindow
            }
            var filter = ""
            for (search in search) {
                if (filter == "")
                    filter +="?${search.type}=${search.data}"
                else
                    filter +="&${search.type}=${search.data}"
            }
            url += filter
            var listImage = mutableListOf<ImgurImage>()
            url.httpGet()
                    .responseString { request, response, result ->
                        val (data, error) = result
                        if (error != null)
                            println("ERROR $error")
                        else {
                            listImage = ConvertData.galleryToMutableListImgurImage(data, listImage)
                            val activity = context as GridActivity
                            activity.loadGrid(listImage)
                        }
                    }
            return listImage
        }

        fun getSearchTag(context: Context, tag: String): MutableList<ImgurImage> {
            var listImage = mutableListOf<ImgurImage>()
            "/gallery/t/$tag".httpGet()
                    .responseString { request, response, result ->
                        val (data, error) = result
                        if (error != null)
                            println("ERROR $error")
                        else {
                            var imgurTag = getJsonData(data.toString())
                            imgurTag = JSONObject(imgurTag).getJSONArray("items").toString()
                            imgurTag = "{data:${imgurTag}, \"success\": true}"
                            listImage = ConvertData.galleryToMutableListImgurImage(imgurTag, listImage)
                            val activity = context as GridActivity
                            activity.loadGrid(listImage)
                        }
                    }
            return listImage
        }

        fun getMyImage(context: Context): MutableList<ImgurImage> {
            var listImage = mutableListOf<ImgurImage>()
            "/account/me/images".httpGet()
                    .responseString { request, response, result ->
                        val (data, error) = result
                        if (error != null)
                            println("ERROR $error")
                        else {
                            println("MY:$data")
                            listImage = ConvertData.imagesToMutableListImgurImage(data, listImage)
                            val activity = context as GridActivity
                            activity.loadGrid(listImage)
                        }
                    }
            return listImage
        }

        fun setImageFavorite(context:Context, image: ImgurImage, favorite : Boolean): Boolean {
            Fuel.post("/image/${image.id}/favorite")
                    .response { request, response, result ->
                        val (data, error) = result
                        if (error != null) {
                            println("ERROR $error")
                        }
                        else {
                            val activity = context as ImageActivity
                            activity.setfavorite(!favorite)
                        }
                    }
            return favorite
        }

        fun getMyFavoriteImage(context: Context): MutableList<ImgurImage> {
            var listImage = mutableListOf<ImgurImage>()
            "/account/me/favorites".httpGet()
                    .responseString { request, response, result ->
                        val (data, error) = result
                        if (error != null)
                            println("ERROR $error")
                        else {
                            println("MY:$data")
                            listImage = ConvertData.imagesToMutableListImgurImage(data, listImage)
                            val activity = context as GridActivity
                            activity.loadGrid(listImage)
                        }
                    }
            return listImage
        }

        fun getMetadataFromId(id: String, context: Context): Image {
            val url = "https://api.imgur.com/3/image/$id"
            val request = HttpUtils.createRequest(url, mapOf("Authorization" to "Client-ID ${clientId}"))
            val response = HttpUtils.sendRequest(request)
            val body = response.body()!!
            val jsonResponse = body.string()
            val gson = Gson()
            val imgurJson = getJsonData(jsonResponse)
            val imgurImage = gson.fromJson(imgurJson, ImgurGalleryAlbum::class.java)
            return Image(
                    if (imgurImage.is_album) {
                        imgurImage.cover
                    } else {
                        imgurImage.id
                    },
                    if (imgurImage.title != null) {
                        imgurImage.title
                    } else {
                        imgurImage.id
                    },
                    SecretUtils.getSecrets(context).second.accountUsername,
                    imgurImage.points,
                    imgurImage.datetime,
                    if (imgurImage.is_album) {
                        imgurImage.id
                    } else {
                        ""
                    },
                    imgurImage.is_album
            )
        }
    }
}