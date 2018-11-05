package com.appdev.epitech.epicture.adapters

import android.content.Context
import android.media.Image
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.BaseAdapter
import android.widget.ImageView
import com.appdev.epitech.epicture.R
import com.bumptech.glide.Glide
import android.util.DisplayMetrics


var imageUrls = arrayOf("http://i.imgur.com/rFLNqWIb.jpg", "http://i.imgur.com/C9pBVt7b.jpg", "http://i.imgur.com/rT5vXE1b.jpg", "http://i.imgur.com/aIy5R2kb.jpg", "http://i.imgur.com/MoJs9pTb.jpg", "http://i.imgur.com/S963yEMb.jpg", "http://i.imgur.com/rLR2cycb.jpg", "http://i.imgur.com/SEPdUIxb.jpg", "http://i.imgur.com/aC9OjaMb.jpg", "http://i.imgur.com/76Jfv9bb.jpg", "http://i.imgur.com/fUX7EIBb.jpg", "http://i.imgur.com/syELajxb.jpg", "http://i.imgur.com/COzBnrub.jpg", "http://i.imgur.com/Z3QjilAb.jpg")


class ImageGridAdapter(private val mContext: Context) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun getCount(): Int = imageUrls.size

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0L

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val myImageView: ImageView
        if (null == convertView) {
            myImageView = ImageView(mContext)
            val displayMetrics = myImageView.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels / 3
            myImageView.layoutParams = ViewGroup.LayoutParams(dpWidth, dpWidth)
            myImageView.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            myImageView = convertView as ImageView
        }
        Glide
                .with(mContext)
                .load(imageUrls[position])
                .into(myImageView)

        return myImageView
    }
}