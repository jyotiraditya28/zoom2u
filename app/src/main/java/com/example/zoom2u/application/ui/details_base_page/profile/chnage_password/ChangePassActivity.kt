package com.example.zoom2u.application.ui.details_base_page.profile.chnage_password

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
import com.example.zoom2u.databinding.ActivityChnagePassBinding
import com.example.zoom2u.databinding.ActivitySignUpBinding
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
        repository = ChangePassRepository(serviceApi,onResponseCallback= ::onResponseCallback)
        viewModel.repository = repository
    }
    private fun onResponseCallback(success: String){
    if(success.equals("true"))
     //  DialogActivity.connfrmDialogView(this,"Are you sure!","Do you want change password.",)

        finish()


    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.change_pass_btn->{
               viewModel.changePass(binding.currPass.text.toString(),binding.cnfrmPass.text.toString())
            }

        }
    }


}