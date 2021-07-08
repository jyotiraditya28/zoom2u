package com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.home.delivery_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.profile.my_location.MyLocationRepository
import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.profile.my_location.model.MyLocationResAndEditLocationReq

class DeliveryDetailsViewModel : ViewModel(){

    var success: MutableLiveData<List<MyLocationResAndEditLocationReq>>? = MutableLiveData(null)

    var repository: MyLocationRepository? = null

    fun getMyLocationList() = repository?.getMyLocation(onSuccess = ::onSuccess)

    fun onSuccess(myLocationList:List<MyLocationResAndEditLocationReq>){
        success?.value=myLocationList
    }

    fun getMySuccess(): MutableLiveData<List<MyLocationResAndEditLocationReq>>?{
        return success
    }
}