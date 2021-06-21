package com.example.zoom2u.application.ui.details_base_page.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.zoom2u.R
import com.example.zoom2u.apiclient.ApiClient
import com.example.zoom2u.apiclient.ServiceApi
import com.example.zoom2u.databinding.FragmentProfileBinding
import com.example.zoom2u.application.ui.details_base_page.profile.change_price.PriceChangeActivity
import com.example.zoom2u.application.ui.details_base_page.profile.chnage_password.ChangePassActivity
import com.example.zoom2u.application.ui.details_base_page.profile.my_location.MyLocationActivity
import com.example.zoom2u.application.ui.log_in.LogInActivity
import com.example.zoom2u.application.ui.log_in.LoginRepository
import com.example.zoom2u.application.ui.log_in.LoginViewModel
import com.example.zoom2u.databinding.ActivityBasepageBinding
import com.example.zoom2u.utility.AppPreference
import com.example.zoom2u.utility.Zoom2u

class ProfileFragment : Fragment() , View.OnClickListener{
    lateinit var binding: FragmentProfileBinding
    lateinit var viewModel: ProfileViewModel
    private var repository: ProfileRepository? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         binding = FragmentProfileBinding.inflate(inflater, container, false)
        //binding.changePrice.setOnClickListener(this)
        binding.myLocation.setOnClickListener(this)
        binding.changePass.setOnClickListener(this)
        binding.signOut.setOnClickListener(this)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val serviceApi: ServiceApi = ApiClient.getServices()
        repository = ProfileRepository(serviceApi,onResponseCallback = ::onResponseCallback)
        viewModel.repository = repository

        viewModel.getProfile()

        return binding.root
    }

    private fun onResponseCallback(profileResponce: ProfileResponce) {
     binding.email.setText(profileResponce.Email)
     binding.company.setText(profileResponce.Company)
     binding.phone.setText(profileResponce.Mobile)

    }
    override fun onClick(view: View?) {
        when (view?.id) {
           /* R.id.change_price -> {
                val intent = Intent(activity, PriceChangeActivity::class.java)
                startActivity(intent)
            }*/
            R.id.my_location -> {
                val intent = Intent(activity, MyLocationActivity::class.java)
                startActivity(intent)
            }
            R.id.change_pass ->{
                val intent = Intent(activity, ChangePassActivity::class.java)
                startActivity(intent)
            }
            R.id.sign_out ->{
                AppPreference.getSharedPrefInstance().removeLoginResponce()
                val intent = Intent(activity, LogInActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }

        }
    }
}