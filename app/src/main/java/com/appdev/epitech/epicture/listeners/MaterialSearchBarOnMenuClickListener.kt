package com.appdev.epitech.epicture.listeners

import android.view.MenuItem
import com.appdev.epitech.epicture.GridActivity
import com.appdev.epitech.epicture.R

class MaterialSearchBarOnMenuClickListener(var activity: GridActivity) {
    fun onMenuClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> activity.logoutAction()
            R.id.action_settings -> activity.settingAction()
            R.id.action_myaccount -> activity.accountAction()
            R.id.action_myfavorite -> activity.favoriteAction()
        }
        return false
    }
}