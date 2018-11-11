package com.appdev.epitech.epicture.adapters

import android.content.Context
import android.view.ViewGroup
import android.view.View
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.appdev.epitech.epicture.GridActivity
import com.appdev.epitech.epicture.R
import com.bumptech.glide.Glide
import com.appdev.epitech.epicture.entities.ImgurImage
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_grid.*
import androidx.core.content.ContextCompat.getSystemService
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.grid_content_layout.view.*
import com.etsy.android.grid.util.DynamicHeightImageView
import com.etsy.android.grid.util.DynamicHeightTextView


class ImageGridAdapter(private val mContext: Context,
                       mObjects: MutableList<ImgurImage>) : BaseAdapter() {

    val progressBar = createPlaceholder(mContext)

    private var images: MutableList<ImgurImage>? = mObjects

    override fun getCount(): Int = images!!.size

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0L

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(mContext)
        val vh: ViewHolder
        val view: View
        if (null == convertView) {
            var convertView = inflater.inflate(R.layout.grid_content_layout, null)
            vh = ViewHolder()
            vh.imgView = convertView.imageView
            vh.textView = convertView.textView
            convertView.tag = vh
            view = convertView
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }
        vh.textView!!.text = images!![position].title
        vh.imgView!!.heightRatio = images!![position].height.toDouble()/images!![position].width.toDouble()
        Glide
                .with(mContext)
                .load(images!![position].thumbnailLink)
                .apply(RequestOptions().placeholder(progressBar))
                .into(vh.imgView!!)

        return view
    }

    internal class ViewHolder {
        var imgView: DynamicHeightImageView? = null
        var textView: DynamicHeightTextView? = null
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