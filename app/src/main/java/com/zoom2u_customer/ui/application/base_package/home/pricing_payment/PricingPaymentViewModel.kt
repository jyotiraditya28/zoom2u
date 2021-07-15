package com.zoom2u_customer.ui.application.base_package.home.pricing_payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.ui.application.base_package.home.delivery_details.model.InterStateReq
import com.zoom2u_customer.ui.application.base_package.home.delivery_details.model.IntraStateReq


class PricingPaymentViewModel : ViewModel(){
    var repository:PricingPaymentRepository? = null
    private var intraStateSuccess: MutableLiveData<String>? = MutableLiveData(null)
    private var interStateSuccess: MutableLiveData<String>? = MutableLiveData(null)

    fun getIntraStatePrice(intraStateReq : IntraStateReq?) = repository?.getIntraStatePrice(intraStateReq,onSuccess = ::onIntraSuccess)
    fun getInterStatePrice(interStateReq : InterStateReq?) = repository?.getInterStatePrice(interStateReq,onSuccess = ::onInterSuccess)

    private fun onIntraSuccess(success : String){
        intraStateSuccess?.value=success
    }

    private fun onInterSuccess(success : String){
        interStateSuccess?.value=success
    }

    fun intraStateSuccess():MutableLiveData<String>?{
        return intraStateSuccess
    }

    fun interStateSuccess():MutableLiveData<String>?{
        return interStateSuccess
    }
}