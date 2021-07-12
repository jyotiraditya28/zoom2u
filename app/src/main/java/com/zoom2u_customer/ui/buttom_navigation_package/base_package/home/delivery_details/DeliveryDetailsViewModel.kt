package com.zoom2u_customer.ui.buttom_navigation_package.base_package.home.delivery_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.profile.my_location.MyLocationRepository
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.profile.my_location.edit_add_location.EditAddLocationRepository
import com.zoom2u_customer.ui.buttom_navigation_package.base_package.profile.my_location.model.MyLocationResAndEditLocationReq

class DeliveryDetailsViewModel : ViewModel(){
    var bookDeliveryAlertMsgStr = ""
    var success: MutableLiveData<List<MyLocationResAndEditLocationReq>>? = MutableLiveData(null)
    private var googleAddress : MutableLiveData<String>? = MutableLiveData("")
    var repositoryMyLoc: MyLocationRepository? = null
    var repository: DeliveryDetailsRepository? = null
    var repositoryEditAdd: EditAddLocationRepository? = null
    var isFroPickup:MutableLiveData<Boolean>? = MutableLiveData()


    fun getMyLocationList() = repositoryMyLoc?.getMyLocation(onSuccess = ::onSuccess)

    fun dataFromGoogle(address: String?, isPickup: Boolean) =
        repositoryEditAdd?.getAddressFromGeocoder(address,isPickup,onSuccess = ::googleAddressSuccess)

    fun onSuccess(myLocationList:List<MyLocationResAndEditLocationReq>){
        success?.value=myLocationList
    }

    private fun googleAddressSuccess(msg : String,isPickup: Boolean?){
        googleAddress?.value = msg
        isFroPickup?.value = isPickup
    }

    fun getMySuccess(): MutableLiveData<List<MyLocationResAndEditLocationReq>>?{
        return success
    }

    fun getDataFromGoogleSuccess() : MutableLiveData<String>?{
        return googleAddress
    }

    fun getIsForPickup() :  MutableLiveData<Boolean>?{
        return isFroPickup
    }



}