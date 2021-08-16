package com.zoom2u_customer.utility

import com.zoom2u_customer.ui.application.bottom_navigation.profile.ProfileResponse
import com.zoom2u_customer.ui.log_in.LoginResponce


interface SharedPref {



    fun setLoginResponse(res: String?)

    fun getLoginResponse(): LoginResponce?

    fun removeLoginResponse()

    fun setProfileData(res: String?)

    fun getProfileData(): ProfileResponse?
}