package com.appdev.epitech.epicture.adapters

import android.content.Context
import android.view.ViewGroup
import android.view.View
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.appdev.epitech.epicture.R
import com.bumptech.glide.Glide
import com.appdev.epitech.epicture.entities.ImgurImage
import com.bumptech.glide.request.RequestOptions
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.grid_content_layout.view.*
import com.etsy.android.grid.util.DynamicHeightImageView
import com.etsy.android.grid.util.DynamicHeightTextView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ImageGridAdapter(private val mContext: Context,
                       mObjects: MutableList<ImgurImage>,
                       var listener: OnItemClickListener) : RecyclerView.Adapter<ImageGridAdapter.ViewHolder>() {


    val progressBar = createPlaceholder(mContext)
    private var images: MutableList<ImgurImage>? = mObjects

    override fun getItemCount(): Int = images!!.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.grid_content_layout, parent, false)
        return ViewHolder(view)
    }


    interface OnItemClickListener {
        fun onItemClick(item: ImgurImage)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images!![position], listener)
        if (images!![position].link!!.substringAfterLast(".") == "mp4") {
            holder.iconView!!.visibility = View.VISIBLE
            Glide
                    .with(mContext)
                    .load(mContext.resources.getDrawable(R.drawable.ic_video_icon))
                    .into(holder.iconView!!)
        }
        else if (images!![position].type.contains("gif")) {
            holder.iconView!!.visibility = View.VISIBLE
            Glide
                    .with(mContext)
                    .load(mContext.resources.getDrawable(R.drawable.ic_gif_icon))
                    .into(holder.iconView!!)
        }
        else
            holder.iconView!!.visibility = View.GONE
        holder.textView!!.text = images!![position].title
        holder.imgView!!.heightRatio = images!![position].height.toDouble() / images!![position].width.toDouble()
        Glide
                .with(mContext)
                .load(images!![position].thumbnailLink)
                .apply(RequestOptions().placeholder(progressBar))
                .into(holder.imgView!!)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgView: DynamicHeightImageView? = itemView.imageView
        var textView: DynamicHeightTextView? = itemView.textView
        var iconView: ImageView? = itemView.iconView

        fun bind(item: ImgurImage, listener: OnItemClickListener) {
            itemView.setOnClickListener { listener.onItemClick(item) }
        }
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