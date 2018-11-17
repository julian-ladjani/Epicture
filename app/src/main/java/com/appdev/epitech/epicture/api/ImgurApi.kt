package com.appdev.epitech.epicture.api

import android.content.Context
import android.text.BoringLayout
import android.util.Base64
import android.widget.Toast
import com.google.gson.Gson
import com.appdev.epitech.epicture.SecretUtils
import com.appdev.epitech.epicture.data.ConvertData
import com.appdev.epitech.epicture.entities.*
import com.appdev.epitech.epicture.GridActivity
import com.appdev.epitech.epicture.ImageActivity
import com.appdev.epitech.epicture.adapters.ImageGridAdapter
import com.appdev.epitech.epicture.listeners.GridActivityOnRefreshListener
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpDelete
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

        private var imagesFavorite: MutableList<ImgurImage> = mutableListOf()
        private var parameterSearch = ParameterSearch()


        private fun getJsonData(jsonResponse: String): String {
            val responseObject = JSONObject(jsonResponse)
            if (responseObject.getBoolean("success")) {
                return responseObject.get("data").toString()
            } else {
                // Horrible hack, fixme
                return "[]"
            }
        }

        fun getFavorite(): MutableList<ImgurImage> {
            return imagesFavorite
        }

        fun getParameterSearch(): ParameterSearch {
            return parameterSearch
        }

        fun reloadFavorite() {
            imagesFavorite = getMyFavoriteImage(null, false)
        }

        fun getSelfAccount(context: Context): Account? {
            val (isFull, result) = SecretUtils.getSecrets(context)
            var account: Account? = null
            "/3/account/me".httpGet().responseString { request, response, result ->
                //make a GET to https://httpbin.org/get and do something with response
                val (data, error) = result
                if (error != null) {
                    Toast.makeText(context, "Error: Connection failed", Toast.LENGTH_SHORT).show()
                } else {
                    account = ConvertData.stringtoAccount(data)
                }
            }
            return account
        }

        fun getGallery(context: Context, page: Int, nsfwEnabled: Boolean = true): MutableList<ImgurImage> {
            val sectionParam = when (parameterSearch.section) {
                0 -> "hot"
                1 -> "top"
                2 -> "user"
                else -> "hot"
            }

            val sortParam = when (parameterSearch.sort) {
                0 -> "viral"
                1 -> "time"
                else -> "top" // Top (x)
            }

            val timeWindow = when (parameterSearch.time) {
                0 -> "day"
                1 -> "week"
                2 -> "month"
                3 -> "year"
                4 -> "all"
                else -> ""
            }

            var url = "/gallery/$sectionParam/$sortParam/"
            if (!timeWindow.equals("") && sectionParam == "top") {
                url += "$timeWindow/"
            }
            url +=page
            url += "?mature=$nsfwEnabled&album_previews=true"
            println("URL:$url")
            var listImage = mutableListOf<ImgurImage>()
            url.httpGet()
                    .responseString { request, response, result ->
                        //make a GET to https://httpbin.org/get and do something with response
                        val (data, error) = result
                        if (error != null)
                            Toast.makeText(context, "Error: Connection failed", Toast.LENGTH_SHORT).show()
                        else {
                            listImage = ConvertData.galleryToMutableListImgurImage(data, listImage)
                            val activity = context as GridActivity
                            if (page > 0)
                                activity.loadMorePage(listImage)
                            else
                                activity.loadGrid(listImage)
                        }
                    }
            return listImage
        }

        fun uploadImage(context: Context, file: ByteArray) {
            println(file.toString())
            val base64Encoded = Base64.encodeToString(file, Base64.DEFAULT)
            "/image".httpPost(listOf(Pair("image", base64Encoded)))
                    .response { request, response, result ->
                        val (data, error) = result
                        if (error != null)
                            Toast.makeText(context, "Error: Image not upload", Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(context, "Success: Image upload !", Toast.LENGTH_SHORT).show()

                        var activity = context as GridActivity
                        activity.refreshAction()
                    }
        }

        fun deleteImage(context: Context, image: ImgurImage): Boolean {
            "/image/${image.id}".httpDelete()
                    .response { request, response, result ->
                        val (data, error) = result
                        if (error != null) {
                            Toast.makeText(context, "Error: Image not delete", Toast.LENGTH_SHORT).show()
                        }else {
                            Toast.makeText(context, "Success: Image deleted !", Toast.LENGTH_SHORT).show()
                            var activity = context as GridActivity
                            activity.refreshAction()
                        }
                    }
            return true
        }

        fun getSearch(context: Context, search: String, page: Int): MutableList<ImgurImage> {
            parameterSearch.query.removeIf { x -> x.type == "q" }
            parameterSearch.query.add(ParameterQuery("q", search))
            val sortParam = when (parameterSearch.sort) {
                0 -> "viral"
                1 -> "time"
                else -> "top" // Top (x)
            }

            val timeWindow = when (parameterSearch.time) {
                0 -> "day"
                1 -> "week"
                2 -> "month"
                3 -> "year"
                4 -> "all"
                else -> ""
            }

            var url = "gallery/search/$sortParam/"
            if (!timeWindow.equals("") && sortParam == "top") {
                url += "$timeWindow/"
            }
            url +=page
            var filter = ""
            for (search in parameterSearch.query) {
                if (filter == "")
                    filter += "?${search.type}=${search.data}"
                else
                    filter += "&${search.type}=${search.data}"
            }
            url += filter
            var listImage = mutableListOf<ImgurImage>()
            url.httpGet()
                    .responseString { request, response, result ->
                        val (data, error) = result
                        if (error != null)
                            Toast.makeText(context, "Error: Connection failed", Toast.LENGTH_SHORT).show()
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
                            Toast.makeText(context, "Error: Connection failed", Toast.LENGTH_SHORT).show()
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
                            Toast.makeText(context, "Error: Connection failed", Toast.LENGTH_SHORT).show()
                        else {
                            listImage = ConvertData.imagesToMutableListImgurImage(data, listImage)
                            val activity = context as GridActivity
                            activity.loadGrid(listImage)
                        }
                    }
            return listImage
        }

        fun setImageFavorite(context: Context, image: ImgurImage, favorite: Boolean): Boolean {
            Fuel.post("/image/${image.id}/favorite")
                    .response { request, response, result ->
                        val (data, error) = result
                        if (error != null)
                            Toast.makeText(context, "Error: Favorite invalided", Toast.LENGTH_SHORT).show()
                        else {
                            val activity = context as ImageActivity
                            activity.setfavorite(!favorite)
                        }
                    }
            return favorite
        }

        fun getMyFavoriteImage(context: Context?, mode: Boolean): MutableList<ImgurImage> {
            var listImage = mutableListOf<ImgurImage>()
            "/account/me/favorites".httpGet()
                    .responseString { request, response, result ->
                        val (data, error) = result
                        if (error != null && mode)
                            Toast.makeText(context, "Error: Connection failed", Toast.LENGTH_SHORT).show()
                        else if(data != null) {
                            listImage = ConvertData.imagesToMutableListImgurImage(data, listImage)
                            if (mode) {
                                val activity = context as GridActivity
                                activity.loadGrid(listImage)
                            }
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