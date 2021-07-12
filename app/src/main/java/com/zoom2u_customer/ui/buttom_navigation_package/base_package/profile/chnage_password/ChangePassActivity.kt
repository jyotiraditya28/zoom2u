package com.zoom2u_customer.ui.buttom_navigation_package.base_package.profile.chnage_password

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.ActivityChnagePassBinding
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity


class ChangePassActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityChnagePassBinding
    lateinit var viewModel: ChangePassViewModel
    private var repository: ChangePassRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chnage_pass)

        binding.changePassBtn.setOnClickListener(this)

        viewModel = ViewModelProvider(this).get(ChangePassViewModel::class.java)
        val serviceApi: ServiceApi = com.zoom2u_customer.apiclient.ApiClient.getServices()
        repository = ChangePassRepository(serviceApi,this,onResponseCallback= ::onResponseCallback)
        viewModel.repository = repository
    }
    private fun onResponseCallback(msg: String){
    if(msg == "true"){
        AppUtility.progressBarDissMiss()
        finish()
    }else{
        DialogActivity.alertDialogSingleButton(this, "Alert!", msg)
    }
    }
    private fun onOkClick(){
        viewModel.changePass(binding.currPass.text.toString(),binding.cnfrmPass.text.toString())
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.change_pass_btn->{
                hideKeyboard(this)
                if(checkValidation(binding.currPass.text.toString(),binding.newPass.text.toString(),binding.cnfrmPass.text.toString()))
                   DialogActivity.logoutDialog(this,"Confirmation !","Are you sure you want to change password?", "Ok","Cancel",
                       onCancelClick=::onCancelClick,
                       onOkClick = ::onOkClick)

            }

        }
    }
    private fun onCancelClick(){

    }

    private fun checkValidation(old_pass: String, new_pass: String, confrm_new_pass: String):Boolean {

        if (old_pass == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Current should not be empty.")
            AppUtility.validateTextField(binding.currPass)
            return false
        }else if (old_pass == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "New password should not be empty.")
            AppUtility.validateTextField(binding.newPass)
            return false
        }
        else if (new_pass.length < 6) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "New Password must be at least 6 characters long")
            AppUtility.validateTextField(binding.currPass)
            return false
        }else if (confrm_new_pass != new_pass) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Password confirmation and Password must match.")
            AppUtility . validateTextField (binding.currPass)
            AppUtility . validateTextField (binding.cnfrmPass)
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