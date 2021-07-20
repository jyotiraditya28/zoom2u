package com.zoom2u_customer.ui.application.base_package.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryViewModel: ViewModel() {

    var success:MutableLiveData<List<HistoryResponse>>? =MutableLiveData(null)
    var repository: HistoryRepository? = null

    fun getHistory() = repository?.getHistoryList(onSuccess = ::onSuccess)

    fun onSuccess(history:List<HistoryResponse>){
        success?.value=history

    }


    fun getHistoryList(): MutableLiveData<List<HistoryResponse>>?{
        return success
    }
}