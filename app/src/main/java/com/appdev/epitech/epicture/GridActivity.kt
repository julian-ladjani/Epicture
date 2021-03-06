package com.appdev.epitech.epicture

import android.Manifest
import android.app.Activity
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
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import com.appdev.epitech.epicture.api.ImgurApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.appdev.epitech.epicture.listeners.*
import kotlinx.android.synthetic.main.searchbar.view.*
import java.io.File

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
    private var gridAlreadyLoad = false
    private var maxPage = false
    private var searchQuery: String? = null
    private var nbPage = 0
    private var _path: File? = Environment.getExternalStorageDirectory()
    private var currentGrid: CurrentGridEnum = CurrentGridEnum.HOME_GRID
    private var recyclerViewTouchDisabler: RecyclerView.OnItemTouchListener = RecyclerViewTouchDisabler()

    fun refreshAction() {
        grid_view_images.addOnItemTouchListener(recyclerViewTouchDisabler)
        grid_view_images.scrollToPosition(grid_view_images.top)
        if (currentGrid == CurrentGridEnum.HOME_GRID)
            homeGridAction()
        if (currentGrid == CurrentGridEnum.FAVORITE_GRID)
            favoriteGridAction()
        if (currentGrid == CurrentGridEnum.UPLOAD_GRID)
            uploadGridAction()
    }

    fun filterAction() {
        val intent = Intent(this,
                FilterActivity::class.java)
        startActivity(intent)
    }

    fun settingAction() {
        val intent = Intent(this,
                SettingsActivity::class.java)
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
        if (currentGrid !== CurrentGridEnum.UPLOAD_GRID || !gridAlreadyLoad) {
            searchBarVisibility(false)
            searchBarUploadMode()
            grid_load_progress.visibility = View.VISIBLE
        } else if (gridAlreadyLoad)
            grid_pull_to_refresh.isRefreshing = true
        images = ImgurApi.getMyImage(this)
        currentGrid = CurrentGridEnum.UPLOAD_GRID
    }

    fun homeGridAction() {
        if (currentGrid !== CurrentGridEnum.HOME_GRID || !gridAlreadyLoad) {
            searchBarVisibility(true)
            searchBarHomeMode()
            grid_load_progress.visibility = View.VISIBLE
        } else if (gridAlreadyLoad)
            grid_pull_to_refresh.isRefreshing = true
        getGallery(0)
        currentGrid = CurrentGridEnum.HOME_GRID
    }

    fun favoriteGridAction() {
        if (currentGrid !== CurrentGridEnum.FAVORITE_GRID || !gridAlreadyLoad) {
            searchBarVisibility(false)
            grid_load_progress.visibility = View.VISIBLE
        } else if (gridAlreadyLoad)
            grid_pull_to_refresh.isRefreshing = true
        images = ImgurApi.getMyFavoriteImage(this, true)
        currentGrid = CurrentGridEnum.FAVORITE_GRID
    }

    fun searchAction(text: String) {
        searchBar!!.clearFocus()
        grid_view_images.requestFocus()
        searchQuery = text
        refreshAction()
    }

    fun uploadAction(permissionResquested: Boolean) {
        if (permissionResquested || haveStoragePermission()) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 2)
        }
    }

    private fun imageClickAction(image: ImgurImage) {
        val i = Intent(this, ImageActivity::class.java)
        if (currentGrid == CurrentGridEnum.UPLOAD_GRID)
            i.putExtra("myImage", true)
        i.putExtra("image", image)
        startActivityForResult(i, 1)
    }

    //creators
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)
        if (savedInstanceState != null) {
            currentGrid = CurrentGridEnum.values()[savedInstanceState.getInt("Grid")]
        }
        ImgurApi.reloadFavorite()
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
        grid_view_images.layoutManager = staggeredGridLayoutManager
        gridAdapter = ImageGridAdapter(
                this,
                mutableListOf<ImgurImage>().apply { addAll(images) },
                object : ImageGridAdapter.OnItemClickListener {
                    override fun onItemClick(item: ImgurImage) {
                        imageClickAction(item)
                    }
                },
                grid_view_images)
        grid_view_images.adapter = gridAdapter
        grid_view_images.layoutManager = staggeredGridLayoutManager
        grid_pull_to_refresh.setOnRefreshListener { GridActivityOnRefreshListener(this).onRefresh() }
    }

    //loader
    fun loadMorePageAction() {
        println("load more")
        if (currentGrid == GridActivity.CurrentGridEnum.HOME_GRID) {
            nbPage++
            getGallery(nbPage)
        }
    }

    fun loadMorePage(images: MutableList<ImgurImage>) {
        if (canLoadMorePage()) {
            if (images.isEmpty())
                maxPage = true
            else {
                println("Size:" + images.size)
                images.addAll(images)
                println("Size:" + images.size)
                gridAdapter!!.addNewValues(images, images.size)
                if (gridAdapter != null) {
                    gridAdapter!!.disableActivityLoading()
                }
            }
        }
    }

    fun loadGrid(images: MutableList<ImgurImage>) {
        if (gridAlreadyLoad) {
            gridAdapter!!.clearAdapter()
            gridAdapter!!.setNewValues(images)
        } else {
            gridAdapter!!.clearAdapter()
            gridAdapter!!.setNewValues(images)
            gridAlreadyLoad = true
        }
        maxPage = false
        nbPage = 0
        if (gridAdapter != null) {
            gridAdapter!!.resetScrollManager()
            if (!images.isNullOrEmpty())
                gridAdapter!!.disableActivityLoading()
        }
        grid_pull_to_refresh.isRefreshing = false
        grid_load_progress.visibility = View.GONE
        grid_view_images.removeOnItemTouchListener(recyclerViewTouchDisabler)
    }

    //dispatcher
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (searchBar != null && searchBar!!.isSearchEnabled) {

            if (ev!!.action == MotionEvent.ACTION_DOWN) {
                val outRect = Rect()
                searchBar!!.cardView.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    searchBar!!.clearFocus()
                    grid_view_images.requestFocus()
                    if (searchQuery == null)
                        disableSearch()
                }

            }

        }
        return super.dispatchTouchEvent(ev)
    }

    fun disableSearch(refresh: Boolean = true) {
        searchBar!!.setPlaceHolder("Search...")
        if (searchBar!!.isEnabled) {
            searchBar!!.clearFocus()
            searchBar!!.disableSearch()
        }
        if (searchQuery != null) {
            searchQuery = null
            if (refresh)
                refreshAction()
        }
    }

    //getters
    fun getSearchBar(): MaterialSearchBar {
        return searchBar!!
    }

    fun getGallery(page: Int) {
        if (searchQuery == null)
            images = ImgurApi.getGallery(this, page, false)
        else
            getGallerySearch(page)
    }

    fun getGallerySearch(page: Int) {
        if (searchQuery == null)
            getGallery(page)
        else {
            images = if (searchQuery!![0] == '#' && searchQuery!!.length >= 2)
                ImgurApi.getSearchTag(this, searchQuery!!.substring(1), page)
            else
                ImgurApi.getSearch(this, searchQuery!!, page)
        }
    }

    fun canShowLoadNoMorePage(): Boolean {
        if (currentGrid == CurrentGridEnum.HOME_GRID && gridAlreadyLoad)
            return true
        return false
    }

    fun canLoadMorePage(): Boolean {
        if (currentGrid == CurrentGridEnum.HOME_GRID && !maxPage && gridAlreadyLoad)
            return true
        return false
    }

    fun getSuggestionAdapter(): SearchBarSuggestionAdapter {
        return suggestionAdapter!!
    }

    //other

    fun searchBarButtonClickAction() {
        if (currentGrid == CurrentGridEnum.UPLOAD_GRID)
            uploadAction(false)
        else if (currentGrid == CurrentGridEnum.HOME_GRID)
            filterAction()
    }

    private fun searchBarHomeMode() {
        if (searchQuery == null) {
            searchBar!!.mt_search.visibility = View.VISIBLE
        }
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
            if (searchQuery == null) {
                searchBar!!.mt_placeholder.visibility = View.VISIBLE
                searchBar!!.mt_search.visibility = View.VISIBLE
            } else {
                searchBar!!.mt_search.visibility = View.GONE
                searchBar!!.mt_clear.visibility = View.VISIBLE
                searchBar!!.mt_editText.visibility = View.VISIBLE
            }
            searchBar!!.mt_nav.visibility = View.VISIBLE
        } else {
            if (searchQuery != null) {
                searchBar!!.mt_clear.visibility = View.GONE
                searchBar!!.mt_editText.visibility = View.GONE
            }
            searchBar!!.mt_placeholder.visibility = View.GONE
            searchBar!!.mt_nav.visibility = View.GONE
            searchBar!!.mt_search.visibility = View.GONE
        }
    }

    //saver
    override fun onSaveInstanceState(savedInstanceState: Bundle?) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState!!.putInt("Grid", currentGrid.ordinal)
    }

    //permission requester
    fun haveStoragePermission(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1234)
                return false
            }
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1234 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    uploadAction(true)
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data!!.getBooleanExtra("deletePicture", false)
                val deleteImage = data.getParcelableExtra<ImgurImage>("image")
                if (result)
                    ImgurApi.deleteImage(this, deleteImage)
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val selectedImageUri = data.data

                val stream = contentResolver.openInputStream(selectedImageUri)

                ImgurApi.uploadImage(this, stream.readBytes())
            }
        }
    }

}
