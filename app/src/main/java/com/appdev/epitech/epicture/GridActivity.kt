package com.appdev.epitech.epicture

import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.appdev.epitech.epicture.adapters.ImageGridAdapter
import com.google.android.material.navigation.NavigationView
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.android.synthetic.main.activity_grid.*
import kotlinx.android.synthetic.main.searchbar.view.*
import androidx.cardview.widget.CardView
import android.view.MotionEvent


class GridActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener {

    var searchBar: MaterialSearchBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)
        searchBar = grid_search_bar
        searchBar!!.setOnSearchActionListener(this)
        searchBar!!.setNavIcon(R.drawable.ic_back_to_search,
                R.drawable.animated_search_to_back,
                R.drawable.animated_back_to_search)
        grid_view_images.adapter = ImageGridAdapter(this)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        return true
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        if (searchBar!!.lastSuggestions.size > 0) {
            val lastSuggestion =
                    searchBar!!.lastSuggestions[0] as CharSequence
            if (lastSuggestion.isEmpty() || lastSuggestion.isBlank())
                searchBar!!.lastSuggestions.removeAt(0)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (searchBar != null && searchBar!!.isSearchEnabled) {

            if (ev!!.action == MotionEvent.ACTION_DOWN) {
                val outRect = Rect()
                searchBar!!.cardView.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    searchBar!!.clearFocus()
                    searchBar!!.disableSearch()
                }

            }

        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onButtonClicked(buttonCode: Int) {
        when (buttonCode) {
            MaterialSearchBar.BUTTON_NAVIGATION -> {
                searchBar!!.enableSearch()
            }
            MaterialSearchBar.BUTTON_SPEECH -> {
            }
            MaterialSearchBar.BUTTON_BACK -> searchBar!!.disableSearch()
        }
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        if (text.isNullOrEmpty() || text!!.isBlank()) {
            searchBar!!.setPlaceHolder("Search...")
        } else {
            searchBar!!.setPlaceHolder(text)
        }
        searchBar!!.clearFocus()
        searchBar!!.disableSearch()
    }
}
