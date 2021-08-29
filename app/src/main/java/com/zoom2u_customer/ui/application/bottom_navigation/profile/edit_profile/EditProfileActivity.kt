package com.zoom2u_customer.ui.application.bottom_navigation.profile.edit_profile

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.ActivityEditProfileBinding
import com.zoom2u_customer.ui.application.bottom_navigation.profile.ProfileResponse
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class EditProfileActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityEditProfileBinding
    private var profileResponse: ProfileResponse? = null
    lateinit var viewModel: EditProfileViewModel
    private var repository: EditProfileRepository? = null
    private val GALLERY = 1
    private val CAMERA = 2
    private val IMAGE_DIRECTORY = "/demonuts"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        AppUtility.hideKeyBoardClickOutside(binding.parentCl,this)
        AppUtility.hideKeyBoardClickOutside(binding.cl3,this)
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
                showPictureDialog()

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
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, GALLERY)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let { uri ->
                        launchImageCrop(uri)
                    }
                }
                else{
                    Log.e(TAG, "Image selection error: Couldn't select that image from memory." )
                }
            }

            CAMERA ->{
                val thumbnail = data?.extras?.get("data") as Bitmap
                launchImageCrop(AppUtility.getImageUri(this,thumbnail)!!)
                saveImage(thumbnail)

            }

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val resultUri = result.uri
                    Picasso.with(this).load(resultUri).into(binding.dp)
                    viewModel.uploadDp(resultUri.path)
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Log.e(TAG, "Crop error: ${result.getError()}" )
                }
            }
        }
    }


    fun removeImage(){
        binding.dp.setImageDrawable(getDrawable(R.drawable.profile))
        //Picasso.with(this).load("").into(binding.dp)
        viewModel.uploadDp("")
    }

    private fun showPictureDialog() {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Select photo from gallery",
            "Capture photo from camera",
          //  "Remove profile image"
        )
        pictureDialog.setItems(pictureDialogItems,
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    0 -> pickFromGallery()
                    1 -> takePhotoFromCamera()
                    //2 -> removeImage()
                }
            })
        pictureDialog.show()
    }


    private fun launchImageCrop(uri: Uri){
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1920, 1920)
            .setCropShape(CropImageView.CropShape.RECTANGLE) // default is rectangle
            .start(this)
    }
    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    private fun checkValidation(first_name: String, last_name: String, phone: String): Boolean {
        if (first_name == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your first name")
            AppUtility.validateTextField(binding.fname)
            return false
        }  else if (last_name == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your last name")
            AppUtility.validateTextField(binding.lname)
            return false
        }   else if (phone == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your phone number")
            AppUtility.validateTextField(binding.phone)
            return false
        }  else if (phone.length >20) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your valid phone number")
            AppUtility.validateTextField(binding.phone)
            return false
        }

        else if (!phone.matches(("^[\\s0-9\\()\\-\\+]+$").toRegex())) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter valid phone number")
            AppUtility.validateTextField(binding.phone)
            return false
        }
        return true
    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            Environment.getExternalStorageState() + IMAGE_DIRECTORY
        )
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        try {
            val f = File(
                wallpaperDirectory, Calendar.getInstance()
                    .timeInMillis.toString() + ".jpg"
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this, arrayOf(f.path), arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)
            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }


}