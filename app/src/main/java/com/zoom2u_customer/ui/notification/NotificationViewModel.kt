package com.zoom2u_customer.ui.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.ui.application.bottom_navigation.profile.ProfileRepository
import com.zoom2u_customer.ui.application.bottom_navigation.profile.ProfileResponse

class NotificationViewModel : ViewModel() {
    var success: MutableLiveData<RateMeResponse>? = MutableLiveData()
    var feedBackSuccess: MutableLiveData<String>? = MutableLiveData("")
    var repository: NotificationRepository? = null

    fun getRateDetails(bookingId:Int) = repository?.getRateDetails(bookingId ,onSuccess = ::onSuccess)

    fun callServiceToRateCourier( bookingID:Int, rateInt:Int) = repository?.rateYourBooking(bookingID ,rateInt,onSuccess = ::onFeedSuccess)

    fun onSuccess(rateMe:RateMeResponse){
        success?.value=rateMe

    }

    private fun onFeedSuccess(msg:String){
        feedBackSuccess?.value=msg
    }

    fun getRateSuccess(): MutableLiveData<RateMeResponse>?{
        return success
    }

    fun getFeedSuccess():MutableLiveData<String>?{
        return feedBackSuccess
    }

}