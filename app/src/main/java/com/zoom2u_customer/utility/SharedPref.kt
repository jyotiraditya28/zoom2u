package com.zoom2u_customer.utility

import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.profile.ProfileResponse
import com.zoom2u_customer.ui.log_in.LoginResponce


interface SharedPref {



    fun setLoginResponse(res: String?)

    fun getLoginResponse(): LoginResponce?

    fun removeLoginResponse()

    fun setProfileData(res: String?)

    fun getProfileData(): ProfileResponse?
}