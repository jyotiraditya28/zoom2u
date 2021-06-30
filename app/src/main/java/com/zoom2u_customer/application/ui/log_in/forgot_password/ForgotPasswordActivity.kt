package com.zoom2u_customer.application.ui.log_in.forgot_password


import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.ActivityForgotPasswordBinding
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity


class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityForgotPasswordBinding

    lateinit var viewModel: ForgotPassViewModel
    private var repository: ForgotPassRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        binding.backIcon.setOnClickListener(this)
        binding.resetPassBtn.setOnClickListener(this)

        viewModel = ViewModelProviders.of(this).get(ForgotPassViewModel::class.java)
        val serviceApi: ServiceApi = com.zoom2u_customer.apiclient.ApiClient.getServices()
        repository =
            ForgotPassRepository(serviceApi, this, onResponseCallback = ::onResponseCallback)
        viewModel.repository = repository
    }

    private fun onResponseCallback(success: String, username: String) {

        AppUtility.progressBarDissMiss()
        if (success == "true") {
            DialogActivity.alertDialogOkCallback(
                this,
                "Success!",
                "We've sent an email to $username with further instructions.",
                onItemClick = ::onItemClick
            )
        }

    }

    private fun onItemClick() {
        finish()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.reset_pass_btn -> {
                hideKeyboard(this)
                if (setValidation(binding.email.text.toString())) {
                    viewModel.reSetPass(binding.email.text.toString())
                }
            }
        }
    }

    fun setValidation(email: String): Boolean {
        if (email == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter registered email.")
            AppUtility.validateTextField(binding.email)
            return false
        } else if (!email.matches(("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").toRegex())) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter valid registered email.")
            AppUtility.validateTextField(binding.email)
            return false
        }
        return true

    }

    fun hideKeyboard(context: Context) {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val v = (context as Activity).currentFocus ?: return
        inputManager.hideSoftInputFromWindow(v.windowToken, 0)
    }
}