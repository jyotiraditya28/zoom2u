package com.zoom2u_customer.ui.bottom_navigation_package.base_package.profile.chnage_password

import androidx.lifecycle.ViewModel

class ChangePassViewModel : ViewModel() {

    var repository: ChangePassRepository? = null

    fun changePass(oldPass:String,newPass:String) = repository?.changePass(oldPass,newPass)
}