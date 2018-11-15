package com.appdev.epitech.epicture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.toolbar.view.*

class FilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        toolbar.title.text = "Filters"
    }

    fun backButtonAction(view: View) {
        onBackPressed()
    }
}
