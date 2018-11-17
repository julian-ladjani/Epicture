package com.appdev.epitech.epicture.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ParameterSearch (
        var section :Int = 0,
        var sort :Int = 0,
        var sortSearch :Int = 0,
        var time :Int = 0,
        var timeSearch: Int = 0,
        var type :Int = 0,
        var size :Int = 0,
        var query: ArrayList<ParameterQuery> = arrayListOf()
) : Parcelable