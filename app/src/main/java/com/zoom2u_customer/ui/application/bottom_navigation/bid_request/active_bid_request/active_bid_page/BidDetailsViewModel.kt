package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.apiclient.GetAddressFromGoogle.GoogleAddressRepository
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.ActiveBidListRepository

class BidDetailsViewModel :ViewModel() {
    var success: MutableLiveData<BidDetailsResponse>?=MutableLiveData()
    private var routeSuccess: MutableLiveData<String> = MutableLiveData("")
    var repository: BidDetailsRepository? = null
    var repositoryGoogleAddress: GoogleAddressRepository? = null


    var cancelSuccess:MutableLiveData<String>? = MutableLiveData("")
    var cancelHeavySuccess:MutableLiveData<String>? = MutableLiveData("")
    var repositoryActive: ActiveBidListRepository? = null

    fun getBidDetails(quoteId: Int?) =
        repository?.getBidDetails(quoteId, onSuccess = ::onBidDetailsSuccess)


    fun getRoute(url: String?) =
        repositoryGoogleAddress?.getRoute(url,onSuccess = ::onSuccessRoute)


    fun onBidDetailsSuccess(bid: BidDetailsResponse) {
        success?.value = bid

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


    fun getBidCancel(Id: Int?, pos: Int?) = repositoryActive?.getBidCancel(Id,pos,onSuccess = ::onBidCancelSuccess)

    fun getHeavyBidCancel(Id: Int?, pos: Int?) = repositoryActive?.getHeavyBidCancel(Id,pos,onSuccess = ::onHeavyBidCancelSuccess)



    private fun onBidCancelSuccess(success:String){
        cancelSuccess?.value=success
    }

    private fun onHeavyBidCancelSuccess(success:String){
        cancelHeavySuccess?.value=success
    }


    fun getBidCancelSuccess():MutableLiveData<String>?{
        return cancelSuccess
    }

    fun getHeavyBidCancelSuccess():MutableLiveData<String>?{
        return cancelHeavySuccess
    }
}