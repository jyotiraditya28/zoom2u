package com.zoom2u_customer.ui.application.bottom_navigation.history.history_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.apiclient.GetAddressFromGoogle.GoogleAddressRepository

class HistoryDetailsViewModel :ViewModel() {
    var success: MutableLiveData<HistoryDetailsResponse>?=MutableLiveData()
    private var routeSuccess: MutableLiveData<String> = MutableLiveData("")
    private var cancelSuccess: MutableLiveData<String> = MutableLiveData("")
    var repository: HistoryDetailsRepository? = null
    var repositoryGoogleAddress: GoogleAddressRepository? = null

    fun setHistoryDetails(bookingRef: String?) =
        repository?.getHistoryDetails(bookingRef, onSuccess = ::onSuccess)

    fun cancelBooking(bookingId: String?) =
        repository?.cancelBooking(bookingId, onSuccess = ::onCancelSuccess)




    fun getRoute(url: String?) =
        repositoryGoogleAddress?.getRoute(url,onSuccess = ::onSuccessRoute)


    fun onSuccess(history: HistoryDetailsResponse) {
        success?.value = history

    }

    private fun onCancelSuccess(cancel: String) {
        cancelSuccess.value = cancel

    }

    private fun onSuccessRoute(route: String) {
        routeSuccess.value=route
    }

    fun getHistoryDetails(): MutableLiveData<HistoryDetailsResponse>? {
        return success
    }

    fun getRouteSuccess():MutableLiveData<String>?{
        return routeSuccess
    }

    fun getCancelBooking():MutableLiveData<String>?{
        return cancelSuccess
    }
}