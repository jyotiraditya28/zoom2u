package com.zoom2u_customer.ui.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.ui.application.bottom_navigation.profile.ProfileRepository
import com.zoom2u_customer.ui.application.bottom_navigation.profile.ProfileResponse

class NotificationViewModel : ViewModel() {
    var success: MutableLiveData<String>? = MutableLiveData("")

    var repository: NotificationRepository? = null

    fun getRateDetails(bookingId:Int) = repository?.getRateDetails(bookingId ,onSuccess = ::onSuccess)




    fun callServiceToRateCourier( bookingID:Int, rateInt:Int) = repository?.rateYourBooking(bookingID ,rateInt,onSuccess = ::onSuccess)

    fun onSuccess(msg: String){
        success?.value=msg

    }

    fun getRateSuccess(): MutableLiveData<String>?{
        return success
    }

}