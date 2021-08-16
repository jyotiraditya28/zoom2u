package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.ui.application.bottom_navigation.history.HistoryRepository
import com.zoom2u_customer.ui.application.bottom_navigation.history.HistoryResponse

class ActiveBidListViewModel : ViewModel() {

    var success: MutableLiveData<List<ActiveBidListResponse>>? = MutableLiveData(null)
    var repository: ActiveBidListRepository? = null

    fun getActiveBidList(page:Int) = repository?.getActiveBidList(page,onSuccess = ::onSuccess)

    fun onSuccess(history:List<ActiveBidListResponse>){
        success?.value=history

    }

    fun getActiveBidListSuccess(): MutableLiveData<List<ActiveBidListResponse>>?{
        return success
    }
}