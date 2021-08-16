package com.zoom2u_customer.ui.application.bottom_navigation.profile.edit_profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.ActivityEditProfileBinding
import com.zoom2u_customer.ui.application.bottom_navigation.profile.ProfileResponse
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import java.util.*


class EditProfileActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityEditProfileBinding
    private var profileResponse: ProfileResponse? = null
    lateinit var viewModel: EditProfileViewModel
    private var repository: EditProfileRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        AppUtility.hideKeyBoardClickOutside(binding.parentCl,this)
        AppUtility.hideKeyboardActivityLunched(this)

        if (intent.hasExtra("profileData")) {
            profileResponse = intent.getParcelableExtra("profileData")
            setDataToView(profileResponse)

        }
        viewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)
        val serviceApi: ServiceApi = com.zoom2u_customer.apiclient.ApiClient.getServices()
        repository = EditProfileRepository(serviceApi, this)
        viewModel.repository = repository


        viewModel.editProfileSuccess()?.observe(this) {
            if(!TextUtils.isEmpty(it)){
                AppUtility.progressBarDissMiss()
                   val profile:ProfileResponse= Gson().fromJson(it, ProfileResponse::class.java)
                    val intent = Intent()
                    intent.putExtra("UpdateProfileData", profile)
                    setResult(2, intent)
                    Toast.makeText(this,"Profile details updated successfully.",Toast.LENGTH_LONG).show()
                    finish()

            }

        }
        viewModel.dpSuccess?.observe(this) {
            if (!TextUtils.isEmpty(it)) {
                AppUtility.progressBarDissMiss()
                DialogActivity.alertDialogSingleButton(this, "Well done!", "Image updated successfully.")
            }
        }
        binding.editDp.setOnClickListener(this)
        binding.saveBtn.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)
    }

    private fun setDataToView(profileResponse: ProfileResponse?) {
        binding.fname.setText(profileResponse?.FirstName)
        binding.lname.setText(profileResponse?.LastName)
        binding.phone.setText(profileResponse?.Mobile)
        binding.company.setText(profileResponse?.Company)
        if(!TextUtils.isEmpty(profileResponse?.Photo)) {
            binding.dp.setImageBitmap(AppUtility.getBitmapFromURL(profileResponse?.Photo))
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.edit_dp -> {
                pickFromGallery()
            }
            R.id.back_btn -> {
                finish()
            }
            R.id.save_btn -> {
                profileResponse?.FirstName=binding.fname.text.toString().trim()
                profileResponse?.LastName=binding.lname.text.toString().trim()
                profileResponse?.Company = binding.company.text.toString().trim()
                profileResponse?.Mobile=binding.phone.text.toString().trim()
                if(checkValidation(binding.fname.text.toString().trim(),binding.lname.text.toString().trim(),binding.phone.text.toString().trim()))
                 viewModel.setProfile(profileResponse)
            }


        }
    }




    private fun pickFromGallery() {
        CropImage.activity().start(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                Picasso.with(this).load(resultUri).into(binding.dp)
                viewModel.uploadDp(resultUri.path)

            }
        }
    }






    private fun checkValidation(first_name: String, last_name: String, phone: String): Boolean {
        if (first_name == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your first name")
            AppUtility.validateTextField(binding.fname)
            return false
        } /*else if (!first_name.matches(("[a-zA-Z ]+").toRegex())) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter alphabets in first name")
            AppUtility.validateTextField(binding.fname)
            return false
        }*/ else if (last_name == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your last name")
            AppUtility.validateTextField(binding.lname)
            return false
        } /*else if (!last_name.matches(("[a-zA-Z ]+").toRegex())) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter alphabets in last name")
            AppUtility.validateTextField(binding.lname)
            return false
        }*/   else if (phone == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your phone number")
            AppUtility.validateTextField(binding.phone)
            return false
        } else if (!phone.matches(("^[\\s0-9\\()\\-\\+]+$").toRegex())) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter valid phone number")
            AppUtility.validateTextField(binding.phone)
            return false
        }
        return true
    }




}