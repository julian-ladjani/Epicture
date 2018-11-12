package com.appdev.epitech.epicture.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ParameterSearch (
        val type :String = "q",
        val data :String = "cat"
) : Parcelable