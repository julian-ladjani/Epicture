package com.appdev.epitech.epicture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.appdev.epitech.epicture.R.id.*
import com.appdev.epitech.epicture.api.ImgurApi
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.toolbar.view.*

class FilterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        saveLists()
        hideCorrectItems()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        toolbar.title.text = "Filters"
        restoreFilters()
        sectionList.onItemSelectedListener = this
        sortList.onItemSelectedListener = this
        dateRangeList.onItemSelectedListener = this
        hideCorrectItems()
    }

    private fun hideCorrectItems() {
        if (ImgurApi.getParameterSearch().section == 1)
            dateRangeCard.visibility = View.VISIBLE
        else
            dateRangeCard.visibility = View.GONE
        if (ImgurApi.getParameterSearch().sortSearch == 2)
            searchDateRangeCard.visibility = View.VISIBLE
        else
            searchDateRangeCard.visibility = View.GONE
    }

    private fun saveLists() {
        ImgurApi.getParameterSearch().section = sectionList.selectedItemPosition
        ImgurApi.getParameterSearch().sort = sortList.selectedItemPosition
        ImgurApi.getParameterSearch().time = dateRangeList.selectedItemPosition
        ImgurApi.getParameterSearch().sortSearch = searchSortList.selectedItemPosition
        ImgurApi.getParameterSearch().timeSearch = searchDateRangeList.selectedItemPosition
        ImgurApi.getParameterSearch().type = formatList.selectedItemPosition
        ImgurApi.getParameterSearch().size = sizeList.selectedItemPosition
    }

    private fun restoreFilters() {
        sectionList.setSelection(ImgurApi.getParameterSearch().section)
        sortList.setSelection(ImgurApi.getParameterSearch().sort)
        dateRangeList.setSelection(ImgurApi.getParameterSearch().time)
        searchSortList.setSelection(ImgurApi.getParameterSearch().sortSearch)
        searchDateRangeList.setSelection(ImgurApi.getParameterSearch().timeSearch)
        formatList.setSelection(ImgurApi.getParameterSearch().type)
        sizeList.setSelection(ImgurApi.getParameterSearch().size)
    }

    fun backButtonAction(view: View) {
        onBackPressed()
    }
}
