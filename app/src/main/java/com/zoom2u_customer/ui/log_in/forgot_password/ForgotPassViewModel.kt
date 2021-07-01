package com.zoom2u_customer.ui.log_in.forgot_password

import androidx.lifecycle.ViewModel


class ForgotPassViewModel : ViewModel(){

    var repository: ForgotPassRepository? = null

    fun reSetPass(username :String) = repository?.setForgotPass(username)
}