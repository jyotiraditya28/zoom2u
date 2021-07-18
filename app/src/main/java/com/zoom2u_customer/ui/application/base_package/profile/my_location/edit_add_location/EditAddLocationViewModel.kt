package com.zoom2u_customer.ui.application.base_package.profile.my_location.edit_add_location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.apiclient.GetAddressFromGoogle.GoogleAddressRepository
import com.zoom2u_customer.ui.application.base_package.profile.my_location.model.AddLocationReq
import com.zoom2u_customer.ui.application.base_package.profile.my_location.model.MyLocationResAndEditLocationReq


class EditAddLocationViewModel : ViewModel() {
    private var editSuccess: MutableLiveData<String>? = MutableLiveData("")
    private var addSuccess: MutableLiveData<String>? = MutableLiveData("")
    private var deleteSuccess: MutableLiveData<String>? = MutableLiveData("")
    private var googleAddressSuccess : MutableLiveData<HashMap<String, Any>>? = MutableLiveData()
    var isForEdit:MutableLiveData<Boolean>? = MutableLiveData()
    var repository: EditAddLocationRepository? = null
    var repositoryGoogleAdd : GoogleAddressRepository?=null
    fun editLocation(myLocationResponse: MyLocationResAndEditLocationReq?) =
        repository?.editLocation(myLocationResponse, onSuccess = ::editLocationSuccess)

    fun addLocation(addLocationReq: AddLocationReq?) =
        repository?.addLocation(addLocationReq, onSuccess = ::addLocationSuccess)

    fun deleteLocation(locationId: Int?) =
        repository?.deleteLocation(locationId, onSuccess = ::deleteLocationSuccess)

   fun dataFromGoogle(address: String?, isEdit: Boolean) =
          repositoryGoogleAdd?.getAddressFromGeocoder(address,isEdit,onSuccess = ::googleAddressSuccess)



    private fun editLocationSuccess(msg: String) {
        editSuccess?.value = msg
    }
    private fun addLocationSuccess(msg: String) {
        addSuccess?.value = msg
    }
    private fun deleteLocationSuccess(msg: String) {
        deleteSuccess?.value = msg
    }

    private fun googleAddressSuccess(address: HashMap<String, Any>, isEdit: Boolean?){
        googleAddressSuccess?.value = address
        isForEdit?.value = isEdit

    }


    fun getEditLocationSuccess(): MutableLiveData<String>? {
        return editSuccess
    }

    fun getAddLocationSuccess(): MutableLiveData<String>? {
        return addSuccess
    }

    fun getDeleteLocationSuccess(): MutableLiveData<String>? {
        return deleteSuccess
    }


    fun getDataFromGoogleSuccess() : MutableLiveData<HashMap<String, Any>>?{
        return googleAddressSuccess
    }

    fun getIsForEdit() :  MutableLiveData<Boolean>?{
        return isForEdit
    }



}