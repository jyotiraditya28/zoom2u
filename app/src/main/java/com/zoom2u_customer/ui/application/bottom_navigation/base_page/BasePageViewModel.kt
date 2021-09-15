package com.zoom2u_customer.ui.application.bottom_navigation.base_page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task

class BasePageViewModel : ViewModel(){

    var success: MutableLiveData<String>? = MutableLiveData("")

    var repository: BasePageRepository? = null

    fun sendDeviceTokenID(lat: Double, lang: Double, token:String) = repository?.sendDeviceTokenID(lat.toString(),lang.toString(),token,onSuccess = ::onSuccess)

    fun onSuccess(msg: String){
        success?.value=msg

    }

    fun getProfileSuccess(): MutableLiveData<String>?{
        return success
    }
}