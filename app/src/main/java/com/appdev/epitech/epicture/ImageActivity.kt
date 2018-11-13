package com.appdev.epitech.epicture

import android.Manifest
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.appdev.epitech.epicture.api.ImgurApi
import com.appdev.epitech.epicture.entities.ImgurImage
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_image.*
import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.Manifest.permission
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Build


class ImageActivity : AppCompatActivity() {

    private var image: ImgurImage? = null
    private var favoriteImage: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        image = intent.getParcelableExtra("image")
        setfavorite(image!!.favorite)
        shareButton.setOnClickListener {
            shareAction()
        }
        downloadButton.setOnClickListener {
            downloadAction(false)
        }
        favoriteButton.setOnClickListener {
            ImgurApi.setImageFavorite(this, image!!, favoriteImage)
        }
        VideoView.setOnPreparedListener { mp ->
            mp.isLooping = true
        }
        toolbar.title = image!!.title
        if (image!!.link!!.substringAfterLast(".") != "mp4") {
            photo_view.visibility = View.VISIBLE
            VideoView.visibility = View.INVISIBLE
            Glide
                    .with(this)
                    .load(image!!.link)
                    .into(photo_view)
        } else {
            photo_view.visibility = View.INVISIBLE
            VideoView.visibility = View.VISIBLE
            VideoView.setVideoURI(Uri.parse(image!!.link))
            VideoView.start()
        }
    }

    fun haveStoragePermission(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return false
            }
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloadAction(true)
        }
    }

    fun downloadAction(permissionResquested: Boolean) {
        if (permissionResquested || haveStoragePermission()) {
            val request = DownloadManager.Request(Uri.parse(image!!.link))
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                    image!!.id + "." + image!!.link!!.substringAfterLast("."))
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.allowScanningByMediaScanner()
            val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
        }
    }

    fun shareAction() {
        val shareIntent = Intent(Intent.ACTION_SEND);
        shareIntent.type = image!!.type
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(image!!.link))
        shareIntent.flags = FLAG_ACTIVITY_NEW_TASK
        startActivity(Intent.createChooser(shareIntent, "Share File Using!"));
    }

    fun setfavorite(bool: Boolean) {
        favoriteImage = bool
        if (bool) {
            favoriteButton.setColorFilter(ContextCompat.getColor(this, R.color.red))
        } else
            favoriteButton.setColorFilter(ContextCompat.getColor(this, R.color.white))
    }

}
