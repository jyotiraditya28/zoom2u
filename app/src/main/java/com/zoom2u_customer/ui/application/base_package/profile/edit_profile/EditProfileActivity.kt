package com.zoom2u_customer.ui.application.base_package.profile.edit_profile

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.google.gson.Gson
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.ui.application.base_package.profile.ProfileResponse
import com.zoom2u_customer.databinding.ActivityEditProfileBinding
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityEditProfileBinding
    private val GALLERY = 1
    private val CAMERA = 2
    private var profileResponse: ProfileResponse? = null

    lateinit var viewModel: EditProfileViewModel
    private var repository: EditProfileRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)

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
                AppUtility.hideKeyboardOnClick(this)
                profileResponse?.FirstName=binding.fname.text.toString().trim()
                profileResponse?.LastName=binding.lname.text.toString().trim()
                profileResponse?.Company = binding.company.text.toString().trim()
                profileResponse?.Mobile=binding.phone.text.toString().trim()
                if(checkValidation(binding.fname.text.toString().trim(),binding.lname.text.toString().trim(),binding.phone.text.toString().trim()))
                 viewModel.setProfile(profileResponse)
            }


        }
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select image from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> if (AppUtility.askCameraTakePicture(this))
                    chooseImageFromGallery()
                1 -> if (AppUtility.askGalleryTakeImagePermission(this))
                    takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }


    private fun chooseImageFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    saveImage(bitmap)
                    binding.dp.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@EditProfileActivity, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            binding.dp.setImageBitmap(thumbnail)
            saveImage(thumbnail)
            //viewModel.uploadDp(thumbnail)
        }
    }

    private fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY
        )
        if (!wallpaperDirectory.exists())
            wallpaperDirectory.mkdirs()
        try {
            val f = File(
                wallpaperDirectory, ((Calendar.getInstance()
                    .timeInMillis).toString() + ".png")
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this, arrayOf(f.path), arrayOf("image/png"), null)
            fo.close()

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }

    companion object {
        private const val IMAGE_DIRECTORY = "/nalhdaf"
    }


    private fun checkValidation(first_name: String, last_name: String, phone: String): Boolean {
        if (first_name == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your first name")
            AppUtility.validateTextField(binding.fname)
            return false
        } else if (!first_name.matches(("[a-zA-Z ]+").toRegex())) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter alphabets in first name")
            AppUtility.validateTextField(binding.fname)
            return false
        } else if (last_name == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your last name")
            AppUtility.validateTextField(binding.lname)
            return false
        } else if (!last_name.matches(("[a-zA-Z ]+").toRegex())) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter alphabets in last name")
            AppUtility.validateTextField(binding.lname)
            return false
        }   else if (phone == "") {
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