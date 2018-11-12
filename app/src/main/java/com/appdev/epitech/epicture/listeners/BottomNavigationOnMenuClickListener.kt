package com.appdev.epitech.epicture.listeners

import android.view.MenuItem
import com.appdev.epitech.epicture.GridActivity
import com.appdev.epitech.epicture.R
import it.sephiroth.android.library.bottomnavigation.BottomNavigation

class BottomNavigationOnMenuClickListener(var activity: GridActivity) :
        BottomNavigation.OnMenuItemSelectionListener {
    override fun onMenuItemSelect(p0: Int, p1: Int, p2: Boolean) {
        when (p1) {
            0 -> activity.homeGridAction()
            1 -> activity.favoriteAction()
            2 -> activity.accountAction()
        }
    }

    override fun onMenuItemReselect(p0: Int, p1: Int, p2: Boolean) {
        activity.refreshAction()
    }

}