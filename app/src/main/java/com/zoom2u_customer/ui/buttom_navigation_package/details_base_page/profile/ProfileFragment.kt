package com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.profile


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProviders

import com.google.gson.Gson
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.ui.splash_screen.LogInSignupMainActivity
import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.profile.chnage_password.ChangePassActivity
import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.profile.edit_profile.EditProfileActivity
import com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.profile.my_location.MyLocationActivity
import com.zoom2u_customer.ui.log_in.LoginResponce
import com.zoom2u_customer.databinding.FragmentProfileBinding
import com.zoom2u_customer.utility.AppPreference
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity


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
        viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel::class.java)
        val serviceApi: ServiceApi = com.zoom2u_customer.apiclient.ApiClient.getServices()
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
        //  if (!profileResponse?.Photo.isNullOrEmpty())
        /* Picasso.with(activity).load(profileResponse?.Photo)
             .placeholder(R.drawable.profile).into(binding.dp)
*/

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
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivityForResult(intent, 2)
            }
            R.id.my_location -> {
                val intent = Intent(activity, MyLocationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }
            R.id.change_pass -> {
                val intent = Intent(activity, ChangePassActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }
            R.id.sign_out -> {
                DialogActivity.alertDialogDoubleButton(
                    activity,
                    "Are you sure!",
                    "Are you want Logout?",
                    onItemClick = ::onItemClick
                )
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === 2) {
            val profile: ProfileResponse? =
                data?.getParcelableExtra<ProfileResponse>("UpdateProfileData")
            if (profile != null)
                setDataToView(profile)
        }
    }

}