package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.active_bid_page.active_bid_offers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ActiveBidOffersViewModel : ViewModel() {
    private var success: MutableLiveData<String>? = MutableLiveData("")
    var repository: ActiveBidOffersRepository? = null


    fun quotePayment(nonce:String,requestId:String,offerId: String,orderNo:String) = repository?.quotePayment(nonce,requestId,offerId,orderNo,onSuccess = ::onSuccess)


    private fun onSuccess(msg:String){
        success?.value=msg

    }


    fun getQuotePayment(): MutableLiveData<String>?{
        return success
    }


}