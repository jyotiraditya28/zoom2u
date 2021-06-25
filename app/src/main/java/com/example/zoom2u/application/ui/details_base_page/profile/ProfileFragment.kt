package com.example.zoom2u.application.ui.details_base_page.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.zoom2u.R
import com.example.zoom2u.apiclient.ApiClient
import com.example.zoom2u.apiclient.ServiceApi
import com.example.zoom2u.application.splash_screen.LogInSignupMainActivity
import com.example.zoom2u.application.ui.details_base_page.profile.chnage_password.ChangePassActivity
import com.example.zoom2u.application.ui.details_base_page.profile.edit_profile.EditProfileActivity
import com.example.zoom2u.application.ui.details_base_page.profile.my_location.MyLocationActivity
import com.example.zoom2u.application.ui.log_in.LoginResponce
import com.example.zoom2u.databinding.FragmentProfileBinding
import com.example.zoom2u.utility.AppPreference
import com.example.zoom2u.utility.AppUtility
import com.example.zoom2u.utility.DialogActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentProfileBinding
    lateinit var viewModel: ProfileViewModel
    private var repository: ProfileRepository? = null
    private var profileResponse: ProfileResponse? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        //binding.changePrice.setOnClickListener(this)
        binding.myLocation.setOnClickListener(this)
        binding.changePass.setOnClickListener(this)
        binding.signOut.setOnClickListener(this)
        binding.edit.setOnClickListener(this)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val serviceApi: ServiceApi = ApiClient.getServices()
        repository = ProfileRepository(serviceApi, activity)
        viewModel.repository = repository

        viewModel.getProfile()

        viewModel.getProfileSuccess()?.observe(viewLifecycleOwner) {
            if (it != null)
                AppUtility.progressBarDissMiss()
                setDataToView(it)

        }

        return binding.root
    }

    private fun setDataToView(profileResponse: ProfileResponse?) {
        this.profileResponse = profileResponse
        binding.email.text = profileResponse?.Email
        binding.company.text = profileResponse?.Company
        binding.phone.text = profileResponse?.Mobile
        binding.name.text = profileResponse?.FirstName + " " + profileResponse?.LastName
        if (!profileResponse?.Photo.isNullOrEmpty())
            Picasso.with(activity).load(profileResponse?.Photo)
                .placeholder(R.drawable.booking_icon_background).into(binding.dp)


    }

    private fun onItemClick() {
        val loginResponce: LoginResponce? = AppPreference.getSharedPrefInstance().getLoginResponse()
        loginResponce?.access_token = ""
        AppPreference.getSharedPrefInstance().setLoginResponse(Gson().toJson(loginResponce))

        val intent = Intent(activity, LogInSignupMainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.edit -> {
                val intent = Intent(activity, EditProfileActivity::class.java)
                intent.putExtra("profileData", profileResponse)
                startActivity(intent)
            }
            R.id.my_location -> {
                val intent = Intent(activity, MyLocationActivity::class.java)
                startActivity(intent)
            }
            R.id.change_pass -> {
                val intent = Intent(activity, ChangePassActivity::class.java)
                startActivity(intent)
            }
            R.id.sign_out -> {
                DialogActivity.confirmDialogView(
                    activity,
                    "Are you sure!",
                    "Are you want Logout?",
                    onItemClick = ::onItemClick
                )
            }

        }
    }


}