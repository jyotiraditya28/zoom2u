package com.zoom2u_customer.ui.application.bottom_navigation.home.pricing_payment.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuoteOptionClass (
  var `$id`: String? = null,
  var `$type`: String? = null,
  var ETA: String? = null,
  var Price: Int? = null,
  var BookingFee: Int? = null,
  var PickupDistance: String? = null,
  var DropDistance: String? = null,
  var PickupPrice: Int? = null,
  var DropPrice: Int? = null,
  var InterstatePrice: Int? = null,
  var DeliverySpeed: String? = null,
  var Distance: String? = null,
  var LiveLocationTracking: Boolean? = null,
  var DirectDriverContact: Boolean? = null,
  var PrinterRequired: Boolean? = null,
  var isAvailable: Boolean? = null,
  var DropDateTime: String? = null,
  var EarliestPickupDateTime: String? = null,
  var isPriceSelect:Boolean?=false
): Parcelable
