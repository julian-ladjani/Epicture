package com.appdev.epitech.epicture

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.appdev.epitech.epicture.api.ImgurApi
import com.appdev.epitech.epicture.entities.ImgurImage
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    private var image: ImgurImage? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        image = intent.getParcelableExtra("image")
        //setfavorite(image!!.favorite)
        //favorite_button.setOnClickListener { ImgurApi.setImageFavorite(this, image!!) }
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
        }
        else {
            photo_view.visibility = View.INVISIBLE
            VideoView.visibility = View.VISIBLE
            VideoView.setVideoURI(Uri.parse(image!!.link))
            VideoView.start()
        }
    }

    fun setfavorite(bool:Boolean) {
        if (bool) {
            //favorite_button.setImageResource(R.drawable.favorite)
        } //else
        //favorite_button.setImageResource(R.drawable.unfavorite)
    }

}
