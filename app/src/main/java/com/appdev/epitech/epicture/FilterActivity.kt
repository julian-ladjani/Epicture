package com.appdev.epitech.epicture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import com.appdev.epitech.epicture.R.id.*
import com.appdev.epitech.epicture.api.ImgurApi
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.toolbar.view.*

class FilterActivity : AppCompatActivity(),
        AdapterView.OnItemSelectedListener,
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        ImgurApi.getParameterSearch().mature = isChecked
    }

    override fun onClick(v: View?) {
        if (v == addTagButton) {
            val tag = editTag.text.toString()
            addTag(tag)
            editTag.text.clear()
        }
    }


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
        restoreChips()
        sectionList.onItemSelectedListener = this
        sortList.onItemSelectedListener = this
        dateRangeList.onItemSelectedListener = this
        searchSortList.onItemSelectedListener = this
        searchDateRangeList.onItemSelectedListener = this
        addTagButton.setOnClickListener(this)
        formatList.onItemSelectedListener = this
        sizeList.onItemSelectedListener = this
        nsfwSwitch.setOnCheckedChangeListener(this)
        nsfwSwitch.isChecked = ImgurApi.getParameterSearch().mature
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

    private fun restoreChips() {
        ImgurApi.getParameterSearch().tags.forEach { action -> restoreTag(action) }
    }

    private fun restoreTag(tag: String) {
        val chip = Chip(this)
        chip.text = tag
        //chip.setChipBackgroundColorResource(R.color.chipBackground)
        chip.isCloseIconEnabled = true
        chip.setOnCloseIconClickListener {
            ImgurApi.getParameterSearch().tags.remove(chip.text.toString())
            tagChipGroup.removeView(chip as View)
        }
        tagChipGroup.addView(chip)
        if (!ImgurApi.getParameterSearch().tags.contains(tag))
            ImgurApi.getParameterSearch().tags.add(tag)
    }

    fun addTag(tag: String) {
        val tagChip = createTag(tag)
        if (tagChip != null) {
            tagChipGroup.addView(tagChip)
            if (!ImgurApi.getParameterSearch().tags.contains(tag))
                ImgurApi.getParameterSearch().tags.add(tag)
        }
    }

    fun validateTag(tag: String): Boolean {
        if (tag.isNotBlank() && !ImgurApi.getParameterSearch().tags.contains(tag) &&
                tag.matches(Regex("[a-zA-Z]+")))
            return true
        return false
    }

    fun createTag(tag: String): Chip? {
        if (!validateTag(tag))
            return null
        val chip = Chip(this)
        chip.text = tag
        //chip.setChipBackgroundColorResource(R.color.chipBackground)
        chip.isCloseIconEnabled = true
        chip.setOnCloseIconClickListener {
            ImgurApi.getParameterSearch().tags.remove(chip.text.toString())
            tagChipGroup.removeView(chip as View)
        }
        return chip
    }

    fun backButtonAction(view: View) {
        onBackPressed()
    }
}
