package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.complete_bid_request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request.ActiveBidListResponse
import com.zoom2u_customer.ui.application.bottom_navigation.history.HistoryRepository
import com.zoom2u_customer.ui.application.bottom_navigation.history.HistoryResponse

class CompletedBidListViewModel : ViewModel() {

    var success: MutableLiveData<String> = MutableLiveData("")
    var repository: CompletedBidListRepository? = null

    fun getCompletedBidList(page:Int) = repository?.getCompletedBidList(page,onSuccess = ::onSuccess)

    fun onSuccess(history:String){
        success.value=history

    }

    fun getCompletedBidListSuccess(): MutableLiveData<String>{
        return success
    }
}