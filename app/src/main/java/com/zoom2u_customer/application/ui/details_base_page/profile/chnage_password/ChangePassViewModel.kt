package com.zoom2u_customer.application.ui.details_base_page.profile.chnage_password

import androidx.lifecycle.ViewModel

class ChangePassViewModel : ViewModel() {

    var repository: ChangePassRepository? = null

    fun changePass(oldPass:String,newPass:String) = repository?.changePass(oldPass,newPass)
}