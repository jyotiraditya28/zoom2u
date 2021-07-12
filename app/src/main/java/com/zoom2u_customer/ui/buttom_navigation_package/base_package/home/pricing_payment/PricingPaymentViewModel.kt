package com.zoom2u_customer.ui.buttom_navigation_package.base_package.home.pricing_payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.home.delivery_details.model.IntraStateReq


class PricingPaymentViewModel : ViewModel(){
    private var intraStateSuccess: MutableLiveData<String>? = MutableLiveData(null)
    var repository:PricingPaymentRepository? = null

    fun getIntraStatePrice(intraStateReq : IntraStateReq?) = repository?.getIntraStatePrice(intraStateReq,onSuccess = ::onIntraSuccess)

    private fun onIntraSuccess(success : String){
        intraStateSuccess?.value=success
    }

    fun intraStateSuccess():MutableLiveData<String>?{
        return intraStateSuccess
    }

}