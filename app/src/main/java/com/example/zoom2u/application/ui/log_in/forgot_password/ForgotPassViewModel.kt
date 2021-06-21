package com.example.zoom2u.application.ui.log_in.forgot_password

import androidx.lifecycle.ViewModel


class ForgotPassViewModel : ViewModel(){

    var repository: ForgotPassRepository? = null

    fun reSetPass(username :String) = repository?.setForgotPass(username)
}