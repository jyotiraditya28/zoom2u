package com.zoom2u_customer.ui.application.bottom_navigation.home.getAccountType

import android.os.Parcelable
import com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details.model.InterStateReq
import kotlinx.android.parcel.Parcelize


@Parcelize
data class AccountTypeModel (
    var accountType: String? = null,
    var paymentMethod: String? = null,
    ): Parcelable

