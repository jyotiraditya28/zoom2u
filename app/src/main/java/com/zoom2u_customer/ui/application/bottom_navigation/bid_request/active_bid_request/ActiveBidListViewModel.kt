package com.zoom2u_customer.ui.application.bottom_navigation.bid_request.active_bid_request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.ui.application.bottom_navigation.history.HistoryRepository
import com.zoom2u_customer.ui.application.bottom_navigation.history.HistoryResponse

class ActiveBidListViewModel : ViewModel() {

    var success: MutableLiveData<List<ActiveBidListResponse>>? = MutableLiveData(null)
    var cancelSuccess:MutableLiveData<String>? = MutableLiveData("")
    var cancelHeavySuccess:MutableLiveData<String>? = MutableLiveData("")
    var repository: ActiveBidListRepository? = null

    fun getActiveBidList(page:Int) = repository?.getActiveBidList(page,onSuccess = ::onSuccess)

    fun getBidCancel(Id:Int?) = repository?.getBidCancel(Id,onSuccess = ::onBidCancelSuccess)

    fun getHeavyBidCancel(Id:Int?) = repository?.getHeavyBidCancel(Id,onSuccess = ::onHeavyBidCancelSuccess)

    fun onSuccess(history:List<ActiveBidListResponse>){
        success?.value=history

    }

    private fun onBidCancelSuccess(success:String){
        cancelSuccess?.value=success
    }

    private fun onHeavyBidCancelSuccess(success:String){
        cancelHeavySuccess?.value=success
    }

    fun getActiveBidListSuccess(): MutableLiveData<List<ActiveBidListResponse>>?{
        return success
    }

    fun getBidCancelSuccess():MutableLiveData<String>?{
        return cancelSuccess
    }

    fun getHeavyBidCancelSuccess():MutableLiveData<String>?{
        return cancelHeavySuccess
    }
}