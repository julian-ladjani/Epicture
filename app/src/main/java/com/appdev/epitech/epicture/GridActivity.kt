package com.appdev.epitech.epicture

import android.graphics.Rect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appdev.epitech.epicture.adapters.ImageGridAdapter
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.android.synthetic.main.activity_grid.*
import android.view.MotionEvent
import android.view.View
import com.appdev.epitech.epicture.adapters.SearchBarSuggestionAdapter
import com.appdev.epitech.epicture.R.menu.menu_search_view
import com.appdev.epitech.epicture.entities.ImgurImage
import android.content.Intent
import android.view.MenuItem
import android.widget.AdapterView
import androidx.appcompat.widget.PopupMenu
import android.widget.AdapterView.OnItemClickListener
import com.appdev.epitech.epicture.api.ImgurApi
import com.appdev.epitech.epicture.listeners.GridActivityOnRefreshListener
import com.appdev.epitech.epicture.listeners.MaterialSearchBarOnMenuClickListener
import com.appdev.epitech.epicture.listeners.MaterialSearchBarOnSearchActionListener
import com.appdev.epitech.epicture.listeners.MaterialSearchBarSuggestionOnItemViewClickListener


class GridActivity : AppCompatActivity() {

    private var searchBar: MaterialSearchBar? = null
    private var gridAdapter: ImageGridAdapter? = null
    private var suggestionAdapter: SearchBarSuggestionAdapter? = null
    private lateinit var images: MutableList<ImgurImage>
    private var gridAlreadyLoad = false

    fun refreshAction() {
        images = ImgurApi.getGallery(this, 0, 0, false)
    }

    fun settingAction() {
        val intent = Intent(this,
                SettingActivity::class.java)
        startActivity(intent)
    }

    fun logoutAction() {
        SecretUtils.deleteSecret(this)
        val intent = Intent(this,
                MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
    
    fun accountAction() {
        images = ImgurApi.getMyImage(this)
    }

    fun favoriteAction() {
        images = ImgurApi.getMyFavoriteImage(this)
    }

    fun searchAction(text: String) {
        images = if (text[0] == '#' && text.length >= 2)
            ImgurApi.getSearchTag(this, text.substring(1))
        else
            ImgurApi.getSearch(this, text, 0)
    }

    fun uploadAction(view: View) {

    }

    private fun imageClickAction(parent: AdapterView<*>, v: View, position: Int, id: Long) {
        val i = Intent(this, ImageActivity::class.java)
        i.putExtra("image", images[position])
        startActivity(i)
    }

    //creators
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)
        createSearchBar()
        createUploadButton()
        images = ImgurApi.getGallery(this, 0, 0, false)
    }

    private fun createUploadButton() {
        grid_upload_button.setOnClickListener { View.OnClickListener(this::uploadAction) }
    }

    private fun createSearchBar() {
        suggestionAdapter = SearchBarSuggestionAdapter(layoutInflater)
        searchBar = grid_search_bar
        searchBar!!.setOnSearchActionListener(MaterialSearchBarOnSearchActionListener(this))
        searchBar!!.setCustomSuggestionAdapter(suggestionAdapter)
        searchBar!!.inflateMenu(menu_search_view)
        searchBar!!.menu.setOnMenuItemClickListener(
                PopupMenu.OnMenuItemClickListener(MaterialSearchBarOnMenuClickListener(this)::onMenuClick))
        suggestionAdapter!!.setListener(MaterialSearchBarSuggestionOnItemViewClickListener(this))
        searchBar!!.setNavIcon(R.drawable.ic_back_to_search,
                R.drawable.animated_search_to_back,
                R.drawable.animated_back_to_search)
    }

    private fun createGrid() {
        gridAdapter = ImageGridAdapter(this,
                mutableListOf<ImgurImage>().apply { addAll(images) })
        grid_view_images.adapter = gridAdapter
        grid_view_images.onItemClickListener = OnItemClickListener { parent, v, position, id ->
            imageClickAction(parent, v, position, id)
        }
        grid_pull_to_refresh.setOnRefreshListener { GridActivityOnRefreshListener(this).onRefresh() }
    }
    
    //loader
    fun loadGrid(images: MutableList<ImgurImage>) {
        if (gridAlreadyLoad) {
            gridAdapter!!.clearAdapter()
            gridAdapter!!.setNewValues(images)
            grid_pull_to_refresh.isRefreshing = false
        } else {
            grid_load_progress.visibility = View.GONE
            createGrid()
            gridAlreadyLoad = true
        }
    }

    //dispatcher
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

    //getters
    fun getSearchBar(): MaterialSearchBar {
        return searchBar!!
    }

    fun getSuggestionAdapter(): SearchBarSuggestionAdapter {
        return suggestionAdapter!!
    }
}
