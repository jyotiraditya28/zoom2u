package com.example.zoom2u.application.ui.log_in.forgot_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.zoom2u.R
import com.example.zoom2u.apiclient.ApiClient
import com.example.zoom2u.apiclient.ServiceApi
import com.example.zoom2u.application.ui.details_base_page.BasePageActivity
import com.example.zoom2u.application.ui.sign_up.SignUpRepository
import com.example.zoom2u.application.ui.sign_up.SignUpResponce
import com.example.zoom2u.application.ui.sign_up.SignUpViewModel
import com.example.zoom2u.databinding.ActivityForgotPasswordBinding
import com.example.zoom2u.databinding.ActivityLogInBinding
import com.example.zoom2u.utility.AppUtility

class ForgotPasswordActivity : AppCompatActivity() , View.OnClickListener{
    lateinit var binding: ActivityForgotPasswordBinding

    lateinit var viewModel: ForgotPassViewModel
    private var repository: ForgotPassRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        binding.backIcon.setOnClickListener(this)
        binding.resetPassBtn.setOnClickListener(this)

        viewModel = ViewModelProviders.of(this).get(ForgotPassViewModel::class.java)
        val serviceApi: ServiceApi = ApiClient.getServices()
        repository = ForgotPassRepository(serviceApi,this,onResponseCallback= ::onResponseCallback)
        viewModel.repository = repository
    }

    private fun onResponseCallback(username :String){
        AppUtility.progressBarDissMiss()
        val intent = Intent(this, BasePageActivity::class.java)
        startActivity(intent)
    }
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.reset_pass_btn -> {
                viewModel.reSetPass(binding.email.text.toString())
            }
    }
}}