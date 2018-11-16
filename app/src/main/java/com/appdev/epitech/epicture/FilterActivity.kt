package com.appdev.epitech.epicture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.appdev.epitech.epicture.api.ImgurApi
import com.appdev.epitech.epicture.entities.ParameterSearch
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.search_bar_suggestion.view.*
import kotlinx.android.synthetic.main.toolbar.view.*

class FilterActivity : AppCompatActivity() {

    object selectedListener : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            println("${parent!!.id} $position")
            if (parent.id == 2131362014) {
                ImgurApi.getParameterSearch().section = position
            }
            else if (parent.id == 2131362033) {
                ImgurApi.getParameterSearch().sort = position
            }
            else {
                ImgurApi.getParameterSearch().time = position
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        toolbar.title.text = "Filters"
        sectionList.onItemSelectedListener = selectedListener
        sortList.onItemSelectedListener = selectedListener
        dateRangeList.onItemSelectedListener = selectedListener
    }

    fun backButtonAction(view: View) {
        onBackPressed()
    }
}
