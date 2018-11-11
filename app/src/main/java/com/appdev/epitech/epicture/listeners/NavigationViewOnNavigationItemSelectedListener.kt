package com.appdev.epitech.epicture.listeners

import android.view.MenuItem
import com.appdev.epitech.epicture.GridActivity
import com.appdev.epitech.epicture.api.ImgurApi
import com.google.android.material.navigation.NavigationView
import com.mancj.materialsearchbar.MaterialSearchBar

class NavigationViewOnNavigationItemSelectedListener(var activity: GridActivity):
        NavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        return true
    }
}