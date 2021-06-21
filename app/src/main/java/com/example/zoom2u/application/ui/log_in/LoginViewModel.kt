package com.example.zoom2u.application.ui.log_in

import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel(){

    var repository: LoginRepository? = null

    fun getLogin(loginRequest: LoginRequest) = repository?.getLoginFromRepo(loginRequest)
}