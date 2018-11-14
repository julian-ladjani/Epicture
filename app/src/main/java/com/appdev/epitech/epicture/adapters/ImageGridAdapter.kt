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
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.appdev.epitech.epicture.GridActivity
import com.appdev.epitech.epicture.listeners.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.grid_content_layout.view.*
import com.etsy.android.grid.util.DynamicHeightImageView
import com.etsy.android.grid.util.DynamicHeightTextView
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.grid_progress_bar.view.*

class ImageGridAdapter(private val mContext: Context,
                       mObjects: MutableList<ImgurImage>,
                       var listener: OnItemClickListener,
                       var recyclerView: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mParent: ViewGroup? = null
    private var images: MutableList<ImgurImage>? = mObjects
    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1


    init {
        createOnScrollListener(recyclerView)
        recyclerView.addOnScrollListener(scrollListener!!)
    }

    override fun getItemCount(): Int = images!!.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position > (images!!.size - 1)) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(mContext).inflate(R.layout.grid_content_layout, parent, false)
            mParent = parent
            return ViewHolderRow(view)
        } else {
            println("test")
            val view = LayoutInflater.from(mContext).inflate(R.layout.grid_progress_bar, parent, false)
            return ViewHolderLoading(view)
        }
    }

    private fun createOnScrollListener(recyclerView: RecyclerView) {
        scrollListener = object : EndlessRecyclerViewScrollListener(
                recyclerView.layoutManager as StaggeredGridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                (mContext as GridActivity).loadMorePageAction()
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: ImgurImage)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolderRow) {
            holder.bind(images!![position], listener)
            if (images!![position].link!!.substringAfterLast(".") == "mp4") {
                holder.iconView!!.visibility = View.VISIBLE
                Glide
                        .with(mContext)
                        .load(mContext.resources.getDrawable(R.drawable.ic_video_icon))
                        .into(holder.iconView!!)
            } else if (images!![position].type.contains("gif")) {
                holder.iconView!!.visibility = View.VISIBLE
                Glide
                        .with(mContext)
                        .load(mContext.resources.getDrawable(R.drawable.ic_gif_icon))
                        .into(holder.iconView!!)
            } else
                holder.iconView!!.visibility = View.GONE
            holder.textView!!.text = images!![position].title
            holder.imgView!!.heightRatio = images!![position].height.toDouble() / images!![position].width.toDouble()
            Glide
                    .with(mContext)
                    .load(images!![position].thumbnailLink)
                    .apply(RequestOptions().placeholder(createPlaceholder(mContext)))
                    .into(holder.imgView!!)
        } else {
            val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            layoutParams.isFullSpan = true
            val loadingViewHolder = holder as ViewHolderLoading
            loadingViewHolder.progressBar!!.isIndeterminate = true
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

    fun resetScrollManager() {
        if (scrollListener != null)
            scrollListener!!.resetState()
    }

    fun clearAdapter() {
        images!!.clear()
    }

    fun setNewValues(mObjects: MutableList<ImgurImage>) {
        images!!.addAll(mObjects)
        notifyDataSetChanged()
    }

    private class ViewHolderLoading(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progressBar: ProgressBar? = itemView.grid_load_more_progress
    }

    private class ViewHolderRow(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgView: DynamicHeightImageView? = itemView.imageView
        var textView: DynamicHeightTextView? = itemView.textView
        var iconView: ImageView? = itemView.iconView

        fun bind(item: ImgurImage, listener: OnItemClickListener) {
            itemView.setOnClickListener { listener.onItemClick(item) }
        }
    }
}