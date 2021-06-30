package com.zoom2u_customer.application.ui.details_base_page.profile.my_location.edit_add_location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zoom2u_customer.application.ui.details_base_page.profile.my_location.model.AddLocationReq
import com.zoom2u_customer.application.ui.details_base_page.profile.my_location.model.MyLocationResAndEditLocationReq


class EditAddLocationViewModel : ViewModel() {
    private var editSuccess: MutableLiveData<String>? = MutableLiveData("")
    private var addSuccess: MutableLiveData<String>? = MutableLiveData("")
    private var deleteSuccess: MutableLiveData<String>? = MutableLiveData("")
    var repository: EditAddLocationRepository? = null

    fun editLocation(myLocationResponse: MyLocationResAndEditLocationReq?) =
        repository?.editLocation(myLocationResponse, onSuccess = ::editLocationSuccess)

    fun addLocation(addLocationReq: AddLocationReq?) =
        repository?.addLocation(addLocationReq, onSuccess = ::addLocationSuccess)

    fun deleteLocation(locationId: Int?) =
        repository?.deleteLocation(locationId, onSuccess = ::deleteLocationSuccess)

    private fun editLocationSuccess(msg: String) {
        editSuccess?.value = msg
    }

    fun getEditLocationSuccess(): MutableLiveData<String>? {
        return editSuccess
    }

    private fun addLocationSuccess(msg: String) {
        addSuccess?.value = msg
    }

    fun getAddLocationSuccess(): MutableLiveData<String>? {
        return addSuccess
    }

    private fun deleteLocationSuccess(msg: String) {
        deleteSuccess?.value = msg
    }

    fun getDeleteLocationSuccess(): MutableLiveData<String>? {
        return deleteSuccess
    }
}