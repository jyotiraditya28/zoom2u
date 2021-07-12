package com.zoom2u_customer.ui.buttom_navigation_package.base_package.home.delivery_details.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class IntraStateReq(
    var CurrentDateTime: String? = null,
    var DistanceInMeters: Int? = null,
    var DropAddress: String? = null,
    var DropLocation: DropLocationClass? = null,
    var DropPostcode: String? = null,
    var DropState: String? = null,
    var DropSuburb: String? = null,
    var IsDropAddressManuallyEntered: Boolean? = null,
    var IsPickupAddressManuallyEntered: Boolean? = null,
    var PickupAddress: String? = null,
    var PickupDateTime: String? = null,
    var PickupLocation: PickupLocationClass? = null,
    var PickupPostcode: String? = null,
    var PickupState: String? = null,
    var PickupSuburb: String? = null,
    var Shipments: List<ShipmentsClass>? = null,
    ) : Parcelable{
    @Parcelize
        data class DropLocationClass(
            var Latitude: String? = null,
            var Longitude: String? = null
        ): Parcelable

    @Parcelize
    data class PickupLocationClass(
        var Latitude: String? = null,
        var Longitude: String? = null,
    ): Parcelable

    @Parcelize
    data class ShipmentsClass(
        var Category: String? = null,
        var Quantity: Int? = null,
        var Value: Int? = null,
        var LengthCm: Int? = null,
        var HeightCm: Int? = null,
        var WidthCm: Int? = null,
        var ItemWeightKg: Double? = null,
        var TotalWeightKg: String? = null
    ): Parcelable


}