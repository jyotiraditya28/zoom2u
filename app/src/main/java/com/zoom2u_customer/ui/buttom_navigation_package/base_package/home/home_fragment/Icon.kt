package com.zoom2u_customer.ui.buttom_navigation_package.base_package.home.home_fragment

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Icon (val image:Int,
                 val text : String,
                 val desc : String,
                 var quantity:Int,
                 var weight: Double,
                 var length : Int,
                 var width : Int,
                 var height : Int,
                 var Category:String,
                 var Value:Int,
                 var ItemWeightKg:Int
                 ): Parcelable

