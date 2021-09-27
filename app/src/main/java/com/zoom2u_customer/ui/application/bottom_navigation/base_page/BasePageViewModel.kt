package com.zoom2u_customer.ui.application.bottom_navigation.base_page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.zoom2u_customer.ui.application.bottom_navigation.profile.ProfileRepository
import com.zoom2u_customer.ui.application.bottom_navigation.profile.ProfileResponse

class BasePageViewModel : ViewModel(){

    var success: MutableLiveData<String>? = MutableLiveData("")
    var userDataSuccess: MutableLiveData<ProfileResponse>? = MutableLiveData()
    var profileRepository: ProfileRepository? = null

    var repository: BasePageRepository? = null

    fun sendDeviceTokenID(lat: Double, lang: Double, token:String) = repository?.sendDeviceTokenID(lat.toString(),lang.toString(),token)

    fun sendDeviceTokenIDWithOutLocation(token:String) = repository?.sendDeviceTokenIDWithOutLocation(token)

    fun getProfile() = profileRepository?.getProflie(onSuccess = ::onSuccess)

    fun onSuccess(msg:ProfileResponse){
        userDataSuccess?.value=msg

    }



}