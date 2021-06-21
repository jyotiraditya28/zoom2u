package com.example.zoom2u.application.ui.sign_up

import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {

    var repository: SignUpRepository? = null

    fun getSignUp(signUpRequest: SignUpRequest) = repository?.getSignUpFromRepo(signUpRequest)
}