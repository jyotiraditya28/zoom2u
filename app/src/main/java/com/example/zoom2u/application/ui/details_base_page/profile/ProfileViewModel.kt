package com.example.zoom2u.application.ui.details_base_page.profile

import androidx.lifecycle.ViewModel
import com.example.zoom2u.application.ui.log_in.forgot_password.ForgotPassRepository

class ProfileViewModel :ViewModel(){

    var repository: ProfileRepository? = null

    fun getProfile() = repository?.getProflie()
}