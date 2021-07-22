package com.zoom2u_customer.ui.log_in

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ApiClient
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.ui.application.bottom_navigation.base_page.BasePageActivity
import com.zoom2u_customer.ui.log_in.forgot_password.ForgotPasswordActivity
import com.zoom2u_customer.ui.sign_up.SignUpActivity
import com.zoom2u_customer.databinding.ActivityLogInBinding
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity


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
        repository = LoginRepository(serviceApi,this)
        viewModel.repository = repository


        viewModel.getLoginSuccess()?.observe(this) {
            if (!TextUtils.isEmpty(it)) {
                AppUtility.progressBarDissMiss()
                if (it.equals("true")) {
                    val intent = Intent(this, BasePageActivity::class.java)
                    startActivity(intent)
                    finish()
                } else
                    DialogActivity.alertDialogSingleButton(this, "Alert!", it)

            }
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.login_btn -> {
                setLoginData()

            }
            R.id.register -> {
                val intent = Intent(this, SignUpActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }
            R.id.forget_pass -> {
                val intent = Intent(this, ForgotPasswordActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }
        }
    }


    private fun setLoginData() {

        email = binding.email.text.toString()
        pass = binding.pass.text.toString()

        if (SetValidation(binding.email.text.toString(), binding.pass.text.toString())) {
            viewModel.getLogin(LoginRequest(username = email!!, password = pass!!))
        }


    }



    fun SetValidation(email: String, pass: String): Boolean {
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pass)) {
            AppUtility.validateTextField(binding.email)
            AppUtility.validateTextField(binding.pass)
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter valid email address and password")
            return false
        } else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            if (TextUtils.isEmpty(email)) {
                DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your email address")
                AppUtility.validateTextField(binding.email)
                return false
            } else if (TextUtils.isEmpty(pass)) {
                DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your password")
                AppUtility.validateTextField(binding.pass)
                return false
            }
        } else if (!email.matches(AppUtility.emailPattern)) {
            AppUtility.validateTextField(binding.email)
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your valid email")
            return false
        }
            binding.pass.setBackgroundResource(R.drawable.default_normal)
            binding.email.setBackgroundResource(R.drawable.default_normal)
            return true

    }
    private fun String.matches(emailRegix: String): Boolean {
        return true;
    }


    override fun onBackPressed() {
        super.onBackPressed()
        AppUtility.progressBarDissMiss()
        finish()
    }


}
