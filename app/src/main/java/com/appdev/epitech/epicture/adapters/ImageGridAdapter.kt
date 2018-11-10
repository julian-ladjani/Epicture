package com.appdev.epitech.epicture.adapters

import android.content.Context
import android.view.ViewGroup
import android.view.View
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.appdev.epitech.epicture.R
import com.bumptech.glide.Glide
import com.appdev.epitech.epicture.entities.ImgurImage
import com.bumptech.glide.request.RequestOptions


class ImageGridAdapter(private val mContext: Context,
                       mObjects: MutableList<ImgurImage>) : BaseAdapter() {

    val progressBar = createPlaceholder(mContext)

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
                .apply(RequestOptions().placeholder(progressBar))
                .into(myImageView)

        return myImageView
    }

    private fun createPlaceholder(context: Context) = CircularProgressDrawable(context).also {
        it.setColorSchemeColors(R.color.colorPrimary)
        it.setColorFilter(mContext.resources.getColor(R.color.colorPrimary),
                android.graphics.PorterDuff.Mode.SRC_IN)
        it.strokeWidth = 5f
        it.centerRadius = 30f
        it.start()
    }

    fun clearAdapter() {
        images!!.clear()
    }

    fun setNewValues(mObjects: MutableList<ImgurImage>) {
        images!!.addAll(mObjects)
        notifyDataSetChanged()
    }
}