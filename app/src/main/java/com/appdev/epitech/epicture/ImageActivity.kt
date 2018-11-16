package com.appdev.epitech.epicture

import android.Manifest
import android.app.Activity
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
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.media.MediaPlayer
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import kotlinx.android.synthetic.main.toolbar.view.*


class ImageActivity : AppCompatActivity() {

    private var image: ImgurImage? = null
    private var myPicture: Boolean? = false
    private var favoriteImage: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        image = intent.getParcelableExtra("image")
        myPicture = intent.getBooleanExtra("myImage", false)
        if (myPicture!!)
            deleteButton.visibility = View.VISIBLE
        var filtered = ImgurApi.getFavorite().filter { x -> x.id == image!!.id }
        setfavorite(filtered.count() != 0)
        shareButton.setOnClickListener {
            shareAction()
        }
        downloadButton.setOnClickListener {
            downloadAction(false)
        }
        deleteButton.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("deletePicture", true)
            returnIntent.putExtra("image", image!!)
            setResult(Activity.RESULT_OK,returnIntent)
            finish()
        }
        favoriteButton.setOnClickListener {
            ImgurApi.setImageFavorite(this, image!!, favoriteImage)
            ImgurApi.reloadFavorite()
        }
        VideoView.setOnPreparedListener { mp ->
            mp.isLooping = true
        }
        viewCounter.title = image!!.views
        toolbar.title.text = image!!.title
        if (image!!.link!!.substringAfterLast(".") != "mp4") {
            photo_view.visibility = View.VISIBLE
            VideoView.visibility = View.INVISIBLE
            Glide
                    .with(this)
                    .load(image!!.link)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                                p0: GlideException?,
                                p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?,
                                p3: Boolean): Boolean {
                                    media_load_progress.indeterminateDrawable.setColorFilter(
                                        resources.getColor(R.color.red), PorterDuff.Mode.SRC_IN)
                                    return false
                        }

                        override fun onResourceReady(
                                p0: Drawable?,
                                p1: Any?,
                                p2: com.bumptech.glide.request.target.Target<Drawable>?,
                                p3: DataSource?, p4: Boolean): Boolean {
                            media_load_progress.visibility = View.GONE
                            return false
                        }
                    })
                    .into(photo_view)
        } else {
            photo_view.visibility = View.INVISIBLE
            VideoView.visibility = View.VISIBLE
            VideoView.setVideoURI(Uri.parse(image!!.link))
            VideoView.setOnInfoListener(onInfoToPlayStateListener)
            VideoView.start()
        }
    }

    override fun onResume() {
        super.onResume()
        if (image!!.link!!.substringAfterLast(".") == "mp4") {
            VideoView.start()
            media_load_progress.visibility = View.VISIBLE
        }
    }

    private val onInfoToPlayStateListener = MediaPlayer.OnInfoListener { mp, what, extra ->
        if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
            media_load_progress.visibility = View.GONE
        }
        if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
            media_load_progress.visibility = View.VISIBLE
        }
        if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
            media_load_progress.visibility = View.VISIBLE
        }
        false
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

    fun backButtonAction(view: View) {
        onBackPressed()
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
