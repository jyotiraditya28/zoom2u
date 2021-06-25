package com.example.zoom2u.application.ui.details_base_page.profile.my_location.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class MyLocationResponse(

    val `$id`: String?=null,
    val `$type`: String?=null,
    val DefaultDropoff: Boolean?=null,
    val DefaultPickup: Boolean?=null,
    val Location: location?=null,
    val PreferredLocationId: Int?=null
) : Parcelable {
    @Parcelize
    data class location(
        val `$id`: String?=null,
        val `$type`: String?=null,
        val Address: String?=null,
        val GPSX: String?=null,
        val GPSY: String?=null,
        val ContactName: String?=null,
        val Phone: String?=null,
        val Email: String?=null,
        val Notes: String?=null,
        val Suburb: String?=null,
        val GpsCoordinates: gpsCoordinates?=null,
        val UnitNumber: String?=null,
        val StreetNumber: String?=null,
        val Street: String?=null,
        val State: String?=null,
        val Postcode: String?=null,
        val CompanyName: String?=null
    ): Parcelable  {
        @Parcelize
        data class gpsCoordinates(
            val `$id`: String?=null,
            val `$type`: String?=null,
            val Geography: geography?=null
        ) :Parcelable {
            @Parcelize
            data class geography(
                val `$id`: String?=null,
                val `$type`: String?=null,
                val CoordinateSystemId: Int?=null,
                val WellKnownText: String?=null
            ): Parcelable
        }
    }
}