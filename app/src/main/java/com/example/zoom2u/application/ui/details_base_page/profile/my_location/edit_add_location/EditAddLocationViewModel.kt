package com.example.zoom2u.application.ui.details_base_page.profile.my_location.edit_add_location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zoom2u.application.ui.details_base_page.profile.my_location.model.MyLocationResAndEditLocationReq

class EditAddLocationViewModel :ViewModel(){
    var success: MutableLiveData<String>? = MutableLiveData("")

    var repository: EditAddLocationRepository? = null

    fun getEditAddLocation(myLocationResponse: MyLocationResAndEditLocationReq?) = repository?.getEditAddLocation(myLocationResponse,onSuccess = ::onSuccess)


    fun onSuccess(msg:String){
        success?.value=msg

    }


    fun getEditAddLocSuccess():MutableLiveData<String>?{
        return success
    }
}