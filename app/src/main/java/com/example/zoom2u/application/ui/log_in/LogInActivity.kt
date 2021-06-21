package com.example.zoom2u.application.ui.log_in

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.zoom2u.R
import com.example.zoom2u.apiclient.ApiClient
import com.example.zoom2u.apiclient.ServiceApi

import com.example.zoom2u.application.ui.details_base_page.BasePageActivity
import com.example.zoom2u.application.ui.log_in.forgot_password.ForgotPasswordActivity
import com.example.zoom2u.application.ui.sign_up.SignUpActivity
import com.example.zoom2u.databinding.ActivityLogInBinding
import com.example.zoom2u.utility.AppUtility
import com.example.zoom2u.utility.DialogActivity


class LogInActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityLogInBinding
    lateinit var viewModel: LoginViewModel
    private var repository: LoginRepository? = null

    private var email: String? = null
    private var pass: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)

        binding.loginBtn.setOnClickListener(this)
        binding.register.setOnClickListener(this)
        binding.forgetPass.setOnClickListener(this)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        val serviceApi: ServiceApi = ApiClient.getServices()
        repository = LoginRepository(serviceApi,this, onResponseCallback = ::onResponseCallback)
        viewModel.repository = repository
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.login_btn -> {
                setLoginData()

                /* val intent = Intent(this, BasePageActivity::class.java)
                    startActivity(intent)*/
            }
            R.id.register -> {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.forget_pass -> {
                val intent = Intent(this, ForgotPasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }


    fun setLoginData() {

        email = binding.email.text.toString()
        pass = binding.pass.text.toString()

        if (SetValidation(binding.email.text.toString(), binding.pass.text.toString())) {
            viewModel.getLogin(LoginRequest(username = email!!, password = pass!!))
        }


    }

    private fun onResponseCallback(msg : String) {
        AppUtility.progressBarDissMiss()
        if(msg.equals("true")){
        val intent = Intent(this, BasePageActivity::class.java)
        startActivity(intent)
        }else
            DialogActivity.alertDialogView(this, "Alert!", msg)
            
    }

    fun SetValidation(email: String, pass: String): Boolean {
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pass)) {
            AppUtility.validateTextField(binding.email)
            AppUtility.validateTextField(binding.pass)
            DialogActivity.alertDialogView(this, "Alert!", "Please enter valid email address and password")
            return false
        } else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            if (TextUtils.isEmpty(email)) {
                DialogActivity.alertDialogView(this, "Alert!", "Please enter your email address")
                AppUtility.validateTextField(binding.email)
                return false
            } else if (TextUtils.isEmpty(pass)) {
                DialogActivity.alertDialogView(this, "Alert!", "Please enter your password")
                AppUtility.validateTextField(binding.pass)
                return false
            }
        } else if (!email.matches(AppUtility.emailPattern)) {
            AppUtility.validateTextField(binding.email)
            DialogActivity.alertDialogView(this, "Alert!", "Please enter your valid email")
            return false
        }
            binding.pass.setBackgroundResource(R.drawable.default_normal)
            binding.email.setBackgroundResource(R.drawable.default_normal)
            return true

    }
    private fun String.matches(emailRegix: String): Boolean {
        return true;
    }
}
