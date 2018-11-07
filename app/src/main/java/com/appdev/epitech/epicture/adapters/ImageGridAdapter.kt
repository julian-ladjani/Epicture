package com.appdev.epitech.epicture.adapters

import android.content.Context
import android.view.ViewGroup
import android.view.View
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.appdev.epitech.epicture.entities.ImgurImage

class ImageGridAdapter(private val mContext: Context, mObjects: MutableList<ImgurImage>) :
        BaseAdapter() {

    private var images: MutableList<ImgurImage>? = mObjects

    override fun getCount(): Int = images!!.size

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
                .load(images!![position].thumbnailLink)
                .into(myImageView)

        return myImageView
    }

    fun clearAdapter() {
        images!!.clear()
    }

    fun setNewValues(mObjects: MutableList<ImgurImage>) {
        images!!.addAll(mObjects)
        notifyDataSetChanged()
    }
}