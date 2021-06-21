package com.example.zoom2u.application.ui.details_base_page.home.home_fragment

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Icon (val image:Int ,val text : String,var count:Int ): Parcelable

