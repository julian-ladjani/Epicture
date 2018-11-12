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
import androidx.appcompat.widget.PopupMenu
import com.appdev.epitech.epicture.api.ImgurApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.appdev.epitech.epicture.entities.ParameterSearch
import com.appdev.epitech.epicture.listeners.*
import kotlinx.android.synthetic.main.searchbar.view.*

class GridActivity : AppCompatActivity() {

    enum class CurrentGridEnum {
        HOME_GRID,
        FAVORITE_GRID,
        UPLOAD_GRID
    }

    private var searchBar: MaterialSearchBar? = null
    private var gridAdapter: ImageGridAdapter? = null
    private var suggestionAdapter: SearchBarSuggestionAdapter? = null
    private var images: MutableList<ImgurImage> = mutableListOf()
    private var parameterSearch :ArrayList<ParameterSearch> = arrayListOf()
    private var gridAlreadyLoad = false
    private var currentGrid: CurrentGridEnum = CurrentGridEnum.HOME_GRID

    fun refreshAction() {
        if (currentGrid == CurrentGridEnum.HOME_GRID)
            homeGridAction()
        if (currentGrid == CurrentGridEnum.FAVORITE_GRID)
            favoriteGridAction()
        if (currentGrid == CurrentGridEnum.UPLOAD_GRID)
            uploadGridAction()
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

    fun uploadGridAction() {
        if (currentGrid !== CurrentGridEnum.UPLOAD_GRID) {
            searchBarVisibility(false)
            searchBarUploadMode()
            grid_load_progress.visibility = View.VISIBLE
        }
        else if (gridAlreadyLoad)
            grid_pull_to_refresh.isRefreshing = true
        images = ImgurApi.getMyImage(this)
        currentGrid = CurrentGridEnum.UPLOAD_GRID
    }

    fun homeGridAction() {
        if (currentGrid !== CurrentGridEnum.HOME_GRID) {
            searchBarVisibility(true)
            searchBarHomeMode()
            grid_load_progress.visibility = View.VISIBLE
        }
        else if (gridAlreadyLoad)
            grid_pull_to_refresh.isRefreshing = true
        images = ImgurApi.getGallery(this, 0, 0,0, false)
        currentGrid = CurrentGridEnum.HOME_GRID
    }

    fun favoriteGridAction() {
        if (currentGrid !== CurrentGridEnum.FAVORITE_GRID) {
            searchBarVisibility(false)
            grid_load_progress.visibility = View.VISIBLE
        }
        else if (gridAlreadyLoad)
            grid_pull_to_refresh.isRefreshing = true
        images = ImgurApi.getMyFavoriteImage(this)
        currentGrid = CurrentGridEnum.FAVORITE_GRID
    }

    fun searchAction(text: String) {
        parameterSearch.clear()
        parameterSearch.add(ParameterSearch("q", text))
        images = if (text[0] == '#' && text.length >= 2)
            ImgurApi.getSearchTag(this, text.substring(1))
        else
            ImgurApi.getSearch(this, parameterSearch, 0,0)
    }

    fun uploadAction() {

    }

    private fun imageClickAction(image: ImgurImage) {
        val i = Intent(this, ImageActivity::class.java)
        i.putExtra("image", image)
        startActivity(i)
    }

    //creators
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)
        if (savedInstanceState != null) {
            currentGrid = CurrentGridEnum.values()[savedInstanceState.getInt("Grid")]
        }
        createGrid()
        createSearchBar()
        grid_bottom_navigation.setOnMenuItemClickListener(BottomNavigationOnMenuClickListener(this))
        refreshAction()
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
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        gridAdapter = ImageGridAdapter(
                this,
                mutableListOf<ImgurImage>().apply { addAll(images) },
                object : ImageGridAdapter.OnItemClickListener {
                    override fun onItemClick(item: ImgurImage) {
                        imageClickAction(item)
                    }
                }
        )
        grid_view_images.adapter = gridAdapter
        grid_view_images.layoutManager = staggeredGridLayoutManager
        grid_pull_to_refresh.setOnRefreshListener { GridActivityOnRefreshListener(this).onRefresh() }
    }

    //loader
    fun loadGrid(images: MutableList<ImgurImage>) {
        if (gridAlreadyLoad) {
            gridAdapter!!.clearAdapter()
            gridAdapter!!.setNewValues(images)
        } else {
            gridAdapter!!.clearAdapter()
            gridAdapter!!.setNewValues(images)
            gridAlreadyLoad = true
        }
        grid_pull_to_refresh.isRefreshing = false
        grid_load_progress.visibility = View.GONE
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

    fun searchBarButtonClickAction() {
        if (currentGrid == CurrentGridEnum.UPLOAD_GRID)
            uploadAction()

    }

    private fun searchBarHomeMode() {
        searchBar!!.mt_search.visibility = View.VISIBLE
        searchBar!!.mt_search.setImageResource(R.drawable.ic_filter_icon)
    }

    private fun searchBarUploadMode() {
        searchBar!!.mt_search.visibility = View.VISIBLE
        searchBar!!.mt_search.setImageResource(R.drawable.ic_upload_icon)
    }

    private fun searchBarVisibility(visible: Boolean) {
        searchBar!!.isClickable = visible
        searchBar!!.mt_nav.isClickable = visible
        if (visible) {
            searchBar!!.mt_placeholder.visibility = View.VISIBLE
            searchBar!!.mt_nav.visibility = View.VISIBLE
            searchBar!!.mt_search.visibility = View.VISIBLE
        } else {
            searchBar!!.mt_placeholder.visibility = View.GONE
            searchBar!!.mt_nav.visibility = View.GONE
            searchBar!!.mt_search.visibility = View.GONE
        }
    }

    fun getSuggestionAdapter(): SearchBarSuggestionAdapter {
        return suggestionAdapter!!
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle?) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState!!.putInt("Grid", currentGrid.ordinal)
    }
}
