package com.appdev.epitech.epicture.api

import android.content.Context
import android.util.Base64
import com.google.gson.Gson
import com.appdev.epitech.epicture.SecretUtils
import com.appdev.epitech.epicture.entities.*
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.ResponseBody
import org.json.JSONObject

class ImgurApi {
    companion object {
        val clientId = "7333a4b592aab44"
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

        fun getSelfAccount(accessToken: String): Account {
            var account = Account("null","null",0,0.0)
            "/3/account/me".httpGet().responseString { request, response, result ->
                //make a GET to https://httpbin.org/get and do something with response
                val (data, error) = result
                if (error != null) {
                    println(error)
                }
                else {
                    val gson = Gson()
                    val imgurJson = getJsonData(data.toString())
                    var imgurAccount = gson.fromJson(imgurJson, ImgurAccount::class.java)
                    var bioAccount = ""

                    if (imgurAccount != null) {
                        bioAccount = imgurAccount.bio
                    }
                    account = Account(imgurAccount.url,
                            bioAccount,
                            imgurAccount.created,
                            imgurAccount.reputation
                    )
                }
            }
            return account
        }

        fun getImageFile(id: String): ByteArray {
            val request = HttpUtils.createRequest("https://i.imgur.com/$id.jpg", mapOf())
            val response = HttpUtils.sendRequest(request)
            val body = response.body()
            return when (body) {
                is ResponseBody -> body.bytes()
                else -> byteArrayOf() // body is null
            }
        }

        fun getThumbnailFile(id: String): ByteArray {
            val request = HttpUtils.createRequest("https://i.imgur.com/$id${thumbnailMode}.jpg", mapOf())
            val response = HttpUtils.sendRequest(request)
            val body = response.body()
            return when (body) {
                is ResponseBody -> body.bytes()
                else -> byteArrayOf() // body is null
            }
        }

        fun getGallery(section: Int, sort: Int, nsfwEnabled: Boolean): Array<Image> {
            val sectionParam = when(section) {
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

            var url = "/gallery/$sectionParam/$sortParam/"
            if (!timeWindow.equals("")) {
                url += "$timeWindow/"
            }

            url += "?mature=$nsfwEnabled&album_previews=true"
            println("URL:$url")
            url.httpGet().responseString { request, response, result ->
                //make a GET to https://httpbin.org/get and do something with response
                val (data, error) = result
                if (error != null)
                    println("ERROR $error")
                else {
                    val gson = Gson()
                    println("DATA $data")
                    val imgurJson = getJsonData(data.toString())
                    //var gallery = gson.fromJson(imgurJson, Array<ImgurGalleryAlbum>::class.java)
                    //var list = mutableListOf(gallery.images)
                    /*val galleryImages = gallery
                        .filter { if (it.is_album) { it.cover != null } else { true } }
                        .map {
                            ImgurImage (
                                    if (it.is_album) { it.cover } else { it.id },
                                    if (it.title != null) { it.title } else { "" },
                                    if (it.account_url != null) { it.account_url } else { "" },
                                    it.datetime,
                                    if (it.is_album) { it.id } else { "" },
                                    it.is_album
                            )
                        }*/
                }
            }

            // Do you know why that filter is there? Let me tell you. It's because imgur api is a shameless liar.
            // So, appearently sometimes gallery items don't have a cover image despite them being an album
            // Because of that, if we want null safety, we need to get rid of albums without a cover image
            // And that folks is why you don't blindly trust API documentation.

            return arrayOf<Image>()
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
                SubredditImage (
                        if (it.is_album) { it.cover } else { it.id },
                        it.title,
                        it.datetime
                )
            }

            return subredditImages.toTypedArray()
        }

        fun getComments(image: Image): Array<Comment> {
            val id = if (image.isAlbum) { image.albumId } else { image.id }
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

        fun uploadImage(file: ByteArray, accessToken: String): String {
            val base64Encoded = Base64.encodeToString(file, Base64.DEFAULT)

            val body = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", base64Encoded)
                    .build()

            val request = Request.Builder()
                    .url("https://api.imgur.com/3/image")
                    .header("Authorization", "Client-ID ${clientId}")
                    .header("Authorization", "Bearer $accessToken")
                    .post(body)
                    .build()

            val response = HttpUtils.sendRequest(request)
            val responseBody = response.body()!!
            val jsonResponse = responseBody.string()
            val imgurJson = getJsonData(jsonResponse)
            val imgurObject = JSONObject(imgurJson)
            return imgurObject.optString("link", "N/A")
        }

        fun getSearch(search: String, sort: Int): Array<Image> {
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

            var url = "https://api.imgur.com/3/gallery/search/$sortParam/"
            if (!timeWindow.equals("")) {
                url += timeWindow
            }

            url += "?q=$search"

            val request = HttpUtils.createRequest(url, mapOf("Authorization" to "Client-ID ${clientId}"))
            val response = HttpUtils.sendRequest(request)
            val body = response.body()!!
            val jsonResponse = body.string()
            val imgurJson = getJsonData(jsonResponse)
            val gson = Gson()
            val gallery = gson.fromJson(imgurJson, Array<ImgurGalleryAlbum>::class.java)
            val galleryImages = gallery
                    .filter { if (it.is_album) { it.cover != null } else { true } }
                    .map {
                        Image (
                                if (it.is_album) { it.cover } else { it.id },
                                if (it.title != null) { it.title } else { "" },
                                if (it.account_url != null) { it.account_url } else { "" },
                                it.points,
                                it.datetime,
                                if (it.is_album) { it.id } else { "" },
                                it.is_album
                        )
                    }

            return galleryImages.toTypedArray()
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
            return Image (
                    if (imgurImage.is_album) { imgurImage.cover } else { imgurImage.id },
                    if (imgurImage.title != null) { imgurImage.title } else { imgurImage.id },
                    SecretUtils.getSecrets(context).second.accountUsername,
                    imgurImage.points,
                    imgurImage.datetime,
                    if (imgurImage.is_album) { imgurImage.id } else { "" },
                    imgurImage.is_album
            )
        }
    }
}