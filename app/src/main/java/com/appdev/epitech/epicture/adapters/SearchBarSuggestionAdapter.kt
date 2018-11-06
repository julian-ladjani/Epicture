package com.appdev.epitech.epicture.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import androidx.recyclerview.widget.RecyclerView
import com.appdev.epitech.epicture.R.layout.search_bar_suggestion
import com.appdev.epitech.epicture.adapters.SearchBarSuggestionAdapter.SuggestionHolder


class SearchBarSuggestionAdapter(inflater: LayoutInflater) :
        SuggestionsAdapter<String, SuggestionHolder>(inflater) {
    private var listener: SuggestionsAdapter.OnItemViewClickListener? = null


    fun setListener(listener: SuggestionsAdapter.OnItemViewClickListener) {
        this.listener = listener
    }

    override fun getSingleViewHeight(): Int {
        return 50
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionHolder {
        val view = layoutInflater.inflate(search_bar_suggestion, parent, false)
        return SuggestionHolder(view)
    }

    override fun onBindSuggestionHolder(suggestion: String, holder: SuggestionHolder, position: Int) {
        holder.text.text = getSuggestions()[position]
    }

    inner class SuggestionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView
        private val iv_delete: ImageView

        init {
            text = itemView.findViewById<View>(com.mancj.materialsearchbar.R.id.text) as TextView
            iv_delete = itemView.findViewById<View>(com.mancj.materialsearchbar.R.id.iv_delete) as ImageView
            itemView.setOnClickListener { v ->
                v.tag = getSuggestions()[adapterPosition]
                listener!!.OnItemClickListener(adapterPosition, v)
            }
            iv_delete.setOnClickListener { v ->
                v.tag = getSuggestions()[adapterPosition]
                listener!!.OnItemDeleteListener(adapterPosition, v)
            }
        }
    }

    interface OnItemViewClickListener {
        fun OnItemClickListener(position: Int, v: View)
        fun OnItemDeleteListener(position: Int, v: View)
    }
}