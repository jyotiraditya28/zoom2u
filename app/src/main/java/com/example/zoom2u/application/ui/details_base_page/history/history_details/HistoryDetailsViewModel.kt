package com.example.zoom2u.application.ui.details_base_page.history.history_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zoom2u.application.ui.details_base_page.history.HistoryRepository
import com.example.zoom2u.application.ui.details_base_page.history.HistoryResponse

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