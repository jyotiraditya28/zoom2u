package com.example.zoom2u.application.ui.details_base_page.profile.my_location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zoom2u.application.ui.details_base_page.profile.my_location.model.MyLocationResAndEditLocationReq

class MyLocationViewModel :ViewModel() {
    var success: MutableLiveData<List<MyLocationResAndEditLocationReq>>? = MutableLiveData(null)

    var repository: MyLocationRepository? = null

    fun getMyLocation() = repository?.getMyLocation(onSuccess = ::onSuccess)

    fun onSuccess(myLocationList:List<MyLocationResAndEditLocationReq>){
        success?.value=myLocationList
    }

    fun getMySuccess():MutableLiveData<List<MyLocationResAndEditLocationReq>>?{
        return success
    }

}