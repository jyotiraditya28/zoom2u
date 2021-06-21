package com.example.zoom2u.utility

import com.example.zoom2u.application.ui.log_in.LoginResponce

interface SharedPref {

   fun setLoginData()


    fun setLoginResponse(res: String?)

    fun getLoginResponse(): LoginResponce?

    fun removeLoginResponce()
}