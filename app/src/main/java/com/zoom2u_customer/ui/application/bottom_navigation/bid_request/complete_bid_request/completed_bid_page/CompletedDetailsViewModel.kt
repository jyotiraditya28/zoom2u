package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.apiclient.GetAddressFromGoogle.GoogleAddressRepository
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.CompletedDetailsRepository
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request.completed_bid_page.CompletedDetailsResponse

class CompletedDetailsViewModel :ViewModel() {
    var success: MutableLiveData<CompletedDetailsResponse>?=MutableLiveData()
    private var routeSuccess: MutableLiveData<String> = MutableLiveData("")
    private var cancelSuccess: MutableLiveData<String> = MutableLiveData("")
    var repository: CompletedDetailsRepository? = null
    var repositoryGoogleAddress: GoogleAddressRepository? = null

    fun getBidDetails(quoteId: Int?) =
        repository?.getBidDetails(quoteId, onSuccess = ::onBidDetailsSuccess)

    fun cancelBooking(bookingId: String?) =
        repository?.cancelBooking(bookingId, onSuccess = ::onCancelSuccess)


    fun getRoute(url: String?) =
        repositoryGoogleAddress?.getRoute(url,onSuccess = ::onSuccessRoute)


    fun onBidDetailsSuccess(bid: CompletedDetailsResponse) {
        success?.value = bid

    }

    private fun onCancelSuccess(cancel: String) {
        cancelSuccess.value = cancel

    }

    private fun onSuccessRoute(route: String) {
        routeSuccess.value=route
    }

    fun getBidDetailsSuccess(): MutableLiveData<CompletedDetailsResponse>? {
        return success
    }

    fun getRouteSuccess():MutableLiveData<String>{
        return routeSuccess
    }

    fun getCancelBooking():MutableLiveData<String>{
        return cancelSuccess
    }
}