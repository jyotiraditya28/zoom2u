package com.example.zoom2u.application.ui.log_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel(){
    var success:MutableLiveData<String>? =MutableLiveData("")

    var repository: LoginRepository? = null

    fun getLogin(loginRequest: LoginRequest) = repository?.getLoginFromRepo(loginRequest,onSuccess = ::onSuccess)


    fun onSuccess(msg:String){
        success?.value=msg

    }


    fun getLoginSuccess():MutableLiveData<String>?{
        return success
    }
}