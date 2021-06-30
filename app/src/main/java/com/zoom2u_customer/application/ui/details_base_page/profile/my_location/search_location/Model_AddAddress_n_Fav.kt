package com.zoom2u_customer.application.ui.details_base_page.profile.my_location.search_location

import java.io.Serializable

class Model_AddAddress_n_Fav(
    private var flag_FavAddress: Int?,
    private var placeID: String?,
    private var placeName: String?,
    private var addressName: String?,
    private var distanceMeters: Int?
) : Serializable {

    public fun getFlag_FavAddress(): Int? {
        return flag_FavAddress;
    }

    public fun getPlaceId(): String? {
        return placeID;
    }

    public fun getPlaceName(): String? {
        return placeName;
    }

    public fun getAddressName(): String? {
        return addressName;
    }

    public fun getDistanceMeters(): Int? {
        return distanceMeters;
    }
}