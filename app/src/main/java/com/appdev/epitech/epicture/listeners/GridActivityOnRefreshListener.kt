package com.appdev.epitech.epicture.listeners

import com.appdev.epitech.epicture.GridActivity

class GridActivityOnRefreshListener(var activity: GridActivity) {
    fun onRefresh() {
        activity.refreshAction()
    }
}