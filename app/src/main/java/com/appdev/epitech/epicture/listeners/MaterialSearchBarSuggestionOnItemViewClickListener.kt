package com.appdev.epitech.epicture.listeners

import android.animation.ValueAnimator
import android.view.View
import android.widget.RelativeLayout
import com.appdev.epitech.epicture.GridActivity
import com.appdev.epitech.epicture.R
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter

class MaterialSearchBarSuggestionOnItemViewClickListener(var activity: GridActivity):
        SuggestionsAdapter.OnItemViewClickListener {

    var searchBar = activity.getSearchBar()
    var suggestionAdapter = activity.getSuggestionAdapter()

    override fun OnItemDeleteListener(position: Int, v: View?) {
        if (v!!.tag is String) {
            animateSuggestions(getListHeight(false).toInt(),
                    getListHeight(true).toInt())
            suggestionAdapter.deleteSuggestion(position, v.tag as String)
        }
    }

    override fun OnItemClickListener(position: Int, v: View?) {
        if (v!!.tag is String) {
            searchBar.text = (v.tag as String)
        }
    }

    private fun getListHeight(isSubtraction: Boolean): Float {
        val destiny = activity.resources.displayMetrics.density
        return if (!isSubtraction) suggestionAdapter.listHeight * destiny
        else (suggestionAdapter.itemCount - 1) * suggestionAdapter.singleViewHeight * destiny
    }

    private fun animateSuggestions(from: Int, to: Int) {
        val last = activity.findViewById<View>(R.id.last) as RelativeLayout
        val lp = last.layoutParams
        if (to == 0 && lp.height == 0)
            return
        val animator = ValueAnimator.ofInt(from, to)
        animator.duration = 200
        animator.addUpdateListener { animation ->
            lp.height = animation.animatedValue as Int
            last.layoutParams = lp
        }
        if (suggestionAdapter.itemCount > 0)
            animator.start()
    }
}