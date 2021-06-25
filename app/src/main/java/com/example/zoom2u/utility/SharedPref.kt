package com.example.zoom2u.utility

import com.example.zoom2u.application.ui.details_base_page.profile.ProfileResponse
import com.example.zoom2u.application.ui.log_in.LoginResponce

interface SharedPref {



    fun setLoginResponse(res: String?)

    fun getLoginResponse(): LoginResponce?

    fun removeLoginResponse()

    fun setProfileData(res: String?)

    fun getProfileData(): ProfileResponse?
}