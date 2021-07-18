package com.zoom2u_customer.ui.application.base_package.home.booking_confirmation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.ui.application.base_package.home.delivery_details.model.IntraStateReq
import com.zoom2u_customer.ui.application.base_package.home.delivery_details.model.SaveDeliveryRequestReq
import com.zoom2u_customer.ui.application.base_package.home.pricing_payment.PricingPaymentRepository
import org.json.JSONObject

class BookingConfirmationViewModel : ViewModel() {
    var repository: BookingConfirmationRepository? = null
    private var deliveryRequestSuccess: MutableLiveData<String>? = MutableLiveData(null)


    fun getDeliveryRequest(jObjForPlaceBooking: JSONObject?) = repository?.getSaveDeliveryRequest(jObjForPlaceBooking,onSuccess = ::onDeliverySuccess)


    private fun onDeliverySuccess(success : String){
        deliveryRequestSuccess?.value=success
    }

    fun getDeliverySuccess():MutableLiveData<String>?{
        return deliveryRequestSuccess
    }
}