package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.apiclient.GetAddressFromGoogle.GoogleAddressRepository

class BidDetailsViewModel :ViewModel() {
    var success: MutableLiveData<BidDetailsResponse>?=MutableLiveData()
    private var routeSuccess: MutableLiveData<String> = MutableLiveData("")
    private var cancelSuccess: MutableLiveData<String> = MutableLiveData("")
    var repository: BidDetailsRepository? = null
    var repositoryGoogleAddress: GoogleAddressRepository? = null

    fun getBidDetails(quoteId: Int?) =
        repository?.getBidDetails(quoteId, onSuccess = ::onBidDetailsSuccess)

    fun cancelBooking(bookingId: String?) =
        repository?.cancelBooking(bookingId, onSuccess = ::onCancelSuccess)


    fun getRoute(url: String?) =
        repositoryGoogleAddress?.getRoute(url,onSuccess = ::onSuccessRoute)


    fun onBidDetailsSuccess(bid: BidDetailsResponse) {
        success?.value = bid

    }

    private fun onCancelSuccess(cancel: String) {
        cancelSuccess.value = cancel

    }

    private fun onSuccessRoute(route: String) {
        routeSuccess.value=route
    }

    fun getBidDetailsSuccess(): MutableLiveData<BidDetailsResponse>? {
        return success
    }

    fun getRouteSuccess():MutableLiveData<String>{
        return routeSuccess
    }

    fun getCancelBooking():MutableLiveData<String>{
        return cancelSuccess
    }
}