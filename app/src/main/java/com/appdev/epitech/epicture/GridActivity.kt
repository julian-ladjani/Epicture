package com.appdev.epitech.epicture

import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.appdev.epitech.epicture.adapters.ImageGridAdapter
import com.google.android.material.navigation.NavigationView
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.android.synthetic.main.activity_grid.*
import android.view.MotionEvent
import android.view.View
import com.appdev.epitech.epicture.adapters.SearchBarSuggestionAdapter
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import android.animation.ValueAnimator
import android.widget.RelativeLayout
import com.appdev.epitech.epicture.R.menu.menu_search_view
import com.appdev.epitech.epicture.entities.ImgurImage
import android.content.Intent
import android.widget.AdapterView
import androidx.appcompat.widget.PopupMenu
import android.widget.AdapterView.OnItemClickListener
import com.appdev.epitech.epicture.api.ImgurApi


class GridActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener,
        SuggestionsAdapter.OnItemViewClickListener {

    private var searchBar: MaterialSearchBar? = null
    private var gridAdapter: ImageGridAdapter? = null
    private var suggestionAdapter: SearchBarSuggestionAdapter? = null
    private lateinit var images: MutableList<ImgurImage>
    private var gridAlreadyLoad = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)
        createSearchBar()
        createUploadButton()
        images = ImgurApi.getGallery(this, 0, 0, false)
    }

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

    private fun refreshAction() {
        images = ImgurApi.getGallery(this, 0, 0, false)
    }

    private fun settingAction() {
        val intent = Intent(this,
                SettingActivity::class.java)
        startActivity(intent)
    }

    private fun logoutAction() {
        SecretUtils.deleteSecret(this)
        val intent = Intent(this,
                MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun searchAction() {

    }

    private fun uploadAction(view: View) {

    }

    private fun imageClickAction(parent: AdapterView<*>, v: View, position: Int, id: Long) {
        val i = Intent(this, ImageActivity::class.java)
        i.putExtra("image", images[position])
        startActivity(i)
    }

    private fun createUploadButton() {
        grid_upload_button.setOnClickListener { View.OnClickListener(this::uploadAction) }
    }

    private fun createSearchBar() {
        suggestionAdapter = SearchBarSuggestionAdapter(layoutInflater)
        searchBar = grid_search_bar
        searchBar!!.setOnSearchActionListener(this)
        searchBar!!.setCustomSuggestionAdapter(suggestionAdapter)
        searchBar!!.inflateMenu(menu_search_view)
        searchBar!!.menu.setOnMenuItemClickListener(
                PopupMenu.OnMenuItemClickListener(this::onMenuClick))
        suggestionAdapter!!.setListener(this)
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
        grid_pull_to_refresh.setOnRefreshListener { onRefresh() }
    }

    private fun onMenuClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> logoutAction()
            R.id.action_settings -> settingAction()
        }
        return false
    }

    private fun onRefresh() {
        refreshAction()
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        return true
    }

    override fun onSearchStateChanged(enabled: Boolean) {

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
            if (text[0] == '#' && text.length >= 2)
                images = ImgurApi.getSearchTag(this, text.toString().substring(1))
            else
                images = ImgurApi.getSearch(text.toString(), 0)
            suggestionAdapter!!.addSuggestion(text.toString())
            searchBar!!.setPlaceHolder(text)
        }
        searchBar!!.clearFocus()
        searchBar!!.disableSearch()
        searchAction()
    }

    override fun OnItemDeleteListener(position: Int, v: View?) {
        if (v!!.tag is String) {
            animateSuggestions(getListHeight(false).toInt(),
                    getListHeight(true).toInt())
            suggestionAdapter!!.deleteSuggestion(position, v.tag as String)
        }
    }

    private fun getListHeight(isSubtraction: Boolean): Float {
        val destiny = resources.displayMetrics.density
        return if (!isSubtraction) suggestionAdapter!!.listHeight * destiny
        else (suggestionAdapter!!.itemCount - 1) * suggestionAdapter!!.singleViewHeight * destiny
    }

    private fun animateSuggestions(from: Int, to: Int) {
        val last = findViewById<View>(R.id.last) as RelativeLayout
        val lp = last.layoutParams
        if (to == 0 && lp.height == 0)
            return
        val animator = ValueAnimator.ofInt(from, to)
        animator.duration = 200
        animator.addUpdateListener { animation ->
            lp.height = animation.animatedValue as Int
            last.layoutParams = lp
        }
        if (suggestionAdapter!!.itemCount > 0)
            animator.start()
    }

    override fun OnItemClickListener(position: Int, v: View?) {
        if (v!!.tag is String) {
            searchBar!!.text = (v.tag as String)
        }
    }
}
