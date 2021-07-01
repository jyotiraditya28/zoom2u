package com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.profile.chnage_password

import androidx.lifecycle.ViewModel

class ChangePassViewModel : ViewModel() {

    var repository: ChangePassRepository? = null

    fun changePass(oldPass:String,newPass:String) = repository?.changePass(oldPass,newPass)
}