package com.example.zoom2u.application.ui.details_base_page.profile.chnage_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.zoom2u.R
import com.example.zoom2u.apiclient.ApiClient
import com.example.zoom2u.apiclient.ServiceApi
import com.example.zoom2u.databinding.ActivityChnagePassBinding
import com.example.zoom2u.utility.AppUtility
import com.example.zoom2u.utility.DialogActivity

class ChangePassActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityChnagePassBinding
    lateinit var viewModel: ChangePassViewModel
    private var repository: ChangePassRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chnage_pass)

        binding.changePassBtn.setOnClickListener(this)

        viewModel = ViewModelProviders.of(this).get(ChangePassViewModel::class.java)
        val serviceApi: ServiceApi = ApiClient.getServices()
        repository = ChangePassRepository(serviceApi,this,onResponseCallback= ::onResponseCallback)
        viewModel.repository = repository
    }
    private fun onResponseCallback(msg: String){
    if(msg.equals("true")){
        AppUtility.progressBarDissMiss()
        finish()
    }else{
        DialogActivity.alertDialogView(this, "Alert!", msg)
    }
    }
    private fun onItemClick(){
        viewModel.changePass(binding.currPass.text.toString(),binding.cnfrmPass.text.toString())
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.change_pass_btn->{
               if(checkValidation(binding.currPass.text.toString(),binding.newPass.text.toString(),binding.cnfrmPass.text.toString()))
                   DialogActivity.confirmDialogView(this,"Are you sure!","Do you want change password.",onItemClick = ::onItemClick)

            }

        }
    }

    private fun checkValidation(old_pass: String, new_pass: String, confrm_new_pass: String):Boolean {

        if (old_pass.equals("")) {
            DialogActivity.alertDialogView(this, "Alert!", "Current should not be empty.")
            AppUtility.validateTextField(binding.currPass)
            return false
        }else if (old_pass.equals("")) {
            DialogActivity.alertDialogView(this, "Alert!", "New password should not be empty.")
            AppUtility.validateTextField(binding.newPass)
            return false
        }
        else if (new_pass.length < 6) {
            DialogActivity.alertDialogView(this, "Alert!", "New Password must be at least 6 characters long")
            AppUtility.validateTextField(binding.currPass)
            return false
        }else if (!confrm_new_pass.equals(new_pass)) {
            DialogActivity.alertDialogView(this, "Alert!", "Password confirmation and Password must match.")
            AppUtility . validateTextField (binding.currPass)
            AppUtility . validateTextField (binding.cnfrmPass)
            return false
        }
        return true

    }


}