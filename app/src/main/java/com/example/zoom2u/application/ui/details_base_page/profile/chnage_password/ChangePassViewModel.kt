package com.example.zoom2u.application.ui.details_base_page.profile.chnage_password

import androidx.lifecycle.ViewModel
import com.example.zoom2u.application.ui.details_base_page.profile.ProfileRepository

class ChangePassViewModel : ViewModel() {

    var repository: ChangePassRepository? = null

    fun changePass(oldPass:String,newPass:String) = repository?.changePass(oldPass,newPass)
}