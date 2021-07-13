package com.zoom2u_customer.utility

import com.zoom2u_customer.ui.bottom_navigation_package.base_package.profile.ProfileResponse
import com.zoom2u_customer.ui.log_in.LoginResponce


interface SharedPref {



    fun setLoginResponse(res: String?)

    fun getLoginResponse(): LoginResponce?

    fun removeLoginResponse()

    fun setProfileData(res: String?)

    fun getProfileData(): ProfileResponse?
}