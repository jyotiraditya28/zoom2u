package com.zoom2u_customer.utility

import com.zoom2u_customer.application.ui.details_base_page.profile.ProfileResponse
import com.zoom2u_customer.application.ui.log_in.LoginResponce


interface SharedPref {



    fun setLoginResponse(res: String?)

    fun getLoginResponse(): LoginResponce?

    fun removeLoginResponse()

    fun setProfileData(res: String?)

    fun getProfileData(): ProfileResponse?
}