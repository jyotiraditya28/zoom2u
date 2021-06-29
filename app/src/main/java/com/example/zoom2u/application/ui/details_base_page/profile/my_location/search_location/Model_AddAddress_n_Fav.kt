package com.example.zoom2u.application.ui.details_base_page.profile.my_location.search_location

import java.io.Serializable

class Model_AddAddress_n_Fav : Serializable {

    private var flag_FavAddress: Int? = 0
    private var placeID: String? = null
    private var placeName: String? = null
    private var addressName: String? = null
    private var distanceMeters: Int? = null

    constructor(flag_FavAddress: Int?, placeID: String?, placeName: String?, addressName: String?, distanceMeters: Int?) {
        this.flag_FavAddress = flag_FavAddress
        this.placeID = placeID
        this.placeName = placeName
        this.addressName = addressName
        this.distanceMeters = distanceMeters
    }

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