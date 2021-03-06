package com.zoom2u_customer.ui.application.bottom_navigation.profile


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.FragmentProfileBinding
import com.zoom2u_customer.ui.application.bottom_navigation.profile.chnage_password.ChangePassActivity
import com.zoom2u_customer.ui.application.bottom_navigation.profile.edit_profile.EditProfileActivity
import com.zoom2u_customer.ui.application.bottom_navigation.profile.my_location.MyLocationActivity
import com.zoom2u_customer.ui.log_in.LoginResponce
import com.zoom2u_customer.ui.splash_screen.LogInSignupMainActivity
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
    ): View {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        //binding.changePrice.setOnClickListener(this)
        binding.myLocation.setOnClickListener(this)
        binding.changePass.setOnClickListener(this)
        binding.signOut.setOnClickListener(this)
        binding.edit.setOnClickListener(this)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val serviceApi: ServiceApi = com.zoom2u_customer.apiclient.ApiClient.getServices()
        repository = ProfileRepository(serviceApi, activity)
        viewModel.repository = repository

        viewModel.getProfile()

        viewModel.getProfileSuccess().observe(viewLifecycleOwner) {
            if (!it.isNullOrBlank()) {
               if(it=="false"){
                   AppUtility.progressBarDissMiss()
                   binding.swipeRefresh.isRefreshing = false
               }else{
                   AppUtility.progressBarDissMiss()
                   val listType = object : TypeToken<List<ProfileResponse?>?>() {}.type
                   val list: List<ProfileResponse> =
                       Gson().fromJson(it, listType)
                   binding.swipeRefresh.isRefreshing = false
                   binding.parentCl.visibility = View.VISIBLE
                   binding.edit.visibility = View.VISIBLE
                   setDataToView(list[0])
               }


            }
        }

        binding.swipeRefresh.setOnRefreshListener(OnRefreshListener {
            viewModel.getProfile()
        })




        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setDataToView(profileResponse: ProfileResponse?) {
        this.profileResponse = profileResponse
        binding.email.text = profileResponse?.Email
        if (!TextUtils.isEmpty(profileResponse?.Company))
            binding.company.text = profileResponse?.Company
        else
            binding.company.text = "Company"
        binding.phone.text = profileResponse?.Mobile
        binding.name.text = AppUtility.upperCaseFirst(profileResponse?.FirstName.toString()) + " " + AppUtility.upperCaseFirst(profileResponse?.LastName.toString())

        if (!TextUtils.isEmpty(profileResponse?.Photo)) {
            binding.dp.setImageBitmap(AppUtility.getBitmapFromURL(profileResponse?.Photo))
           // Picasso.get().load(profileResponse?.Photo).into(binding.dp)

        }
    }
    private fun onOkClick() {
        val loginResponse: LoginResponce? = AppPreference.getSharedPrefInstance().getLoginResponse()
        loginResponse?.access_token = ""
        AppPreference.getSharedPrefInstance().setLoginResponse(Gson().toJson(loginResponse))

        val intent = Intent(activity, LogInSignupMainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
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
                DialogActivity.logoutDialog(
                    activity,
                    "Are you sure!",
                    "Are you want Logout?",
                    "Ok","Cancel",
                    onCancelClick=::onCancelClick,
                    onOkClick = ::onOkClick
                )
            }

        }
    }

    private fun onCancelClick(){

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === 2) {
            val profile: ProfileResponse? =
                data?.getParcelableExtra<ProfileResponse>("UpdateProfileData")
            if (profile != null) {
                setDataToView(profile)
                viewModel.getProfile()
            }
        }
    }


}