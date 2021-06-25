package com.example.zoom2u.application.ui.details_base_page.profile.edit_profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zoom2u.application.ui.details_base_page.profile.ProfileResponse

class EditProfileViewModel : ViewModel(){
    var success: MutableLiveData<String>? = MutableLiveData("")

    var repository: EditProfileRepository? = null

    fun setProfile(profileResponse: ProfileResponse?) = repository?.setProflie(profileResponse,onSuccess = ::onSuccess)

    fun onSuccess(msg: String){
        success?.value=msg

    }

    fun getProfileEditSuccess(): MutableLiveData<String>?{
        return success
    }
}