package com.example.zoom2u.application.ui.details_base_page.profile.my_location.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddLocationReq(
    var DefaultDropoff: Boolean? = null,
    var DefaultPickup: Boolean? = null,
    var location: Location1? = null,
    var Location: Location2? = null
) : Parcelable {
    @Parcelize
    data class Location1(
        var Address: String? = null,
        var ContactName: String? = null,
        var Country: String? = null,
        var Gpsx: Double? = null,
        var Gpsy: Double? = null,
        var Phone: String? = null,
        var Street: String? = null
    ) : Parcelable

    @Parcelize
    data class Location2(
        var ContactName: String? = null,
        var Phone: String? = null,
        var Email: String? = null,
        var Address: String? = null,
        var Notes: String? = null,
        var Gpsx: String? = null,
        var Gpsy: String? = null,
        var UnitNumber: String? = null,
        var StreetNumber: String? = null,
        var Street: String? = null,
        var Suburb: String? = null,
        var State: String? = null,
        var Postcode: String? = null
    ) : Parcelable

}


