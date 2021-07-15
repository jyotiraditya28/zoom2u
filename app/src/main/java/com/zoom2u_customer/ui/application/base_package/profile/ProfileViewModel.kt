package com.zoom2u_customer.ui.application.base_package.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel :ViewModel(){
    var success: MutableLiveData<ProfileResponse>? = MutableLiveData()

    var repository: ProfileRepository? = null

    fun getProfile() = repository?.getProflie(onSuccess = ::onSuccess)

    fun onSuccess(msg:ProfileResponse){
        success?.value=msg

    }

    fun getProfileSuccess():MutableLiveData<ProfileResponse>?{
        return success
    }
}