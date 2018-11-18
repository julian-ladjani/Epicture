package com.appdev.epitech.epicture.listeners

import com.appdev.epitech.epicture.GridActivity
import com.mancj.materialsearchbar.MaterialSearchBar

class MaterialSearchBarOnSearchActionListener(var activity: GridActivity) :
        MaterialSearchBar.OnSearchActionListener {
    var searchBar = activity.getSearchBar()
    var suggestionAdapter = activity.getSuggestionAdapter()

    override fun onButtonClicked(buttonCode: Int) {
        when (buttonCode) {
            MaterialSearchBar.BUTTON_NAVIGATION -> {
                searchBar.enableSearch()
            }
            MaterialSearchBar.BUTTON_SPEECH -> activity.searchBarButtonClickAction()
            MaterialSearchBar.BUTTON_BACK -> activity.disableSearch()
        }
    }

    override fun onSearchStateChanged(enabled: Boolean) {

    }

    override fun onSearchConfirmed(text: CharSequence) {
        if (text.isEmpty() || text.isBlank()) {
            searchBar.setPlaceHolder("Search...")
            activity.disableSearch()
        } else {
            searchBar.setPlaceHolder(text)
            suggestionAdapter.addSuggestion(text.toString())
            searchBar.clearFocus()
            activity.searchAction(text.toString())
        }
    }
}