package com.zoom2u_customer.ui.buttom_navigation_package.base_package.history.history_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryDetailsViewModel :ViewModel() {
    var success: MutableLiveData<HistoryDetailsResponse>?=MutableLiveData()
    var repository: HistoryDetailsRepository? = null

    fun setHistoryDetails(bookingRef: String?) =
        repository?.getHistoryDetails(bookingRef, onSuccess = ::onSuccess)

    fun onSuccess(history: HistoryDetailsResponse) {
        success?.value = history

    }

    fun getHistoryDetails(): MutableLiveData<HistoryDetailsResponse>? {
        return success
    }
}