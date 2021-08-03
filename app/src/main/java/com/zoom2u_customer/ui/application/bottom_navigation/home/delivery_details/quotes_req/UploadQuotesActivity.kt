package com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details.quotes_req

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.zoom2u_customer.R
import com.zoom2u_customer.databinding.ActivityUploadQuotesBinding
import com.zoom2u_customer.utility.AppUtility
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UploadQuotesActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityUploadQuotesBinding
    var selectProfileImgDialog: Dialog? = null
    val PICK_FROM_CAMERA = 1
    val PICK_FROM_GALLERY = 2
    var count = 1
    var arrayOfImageFiles: ArrayList<String>? = null
    var arrayOfcameraImgFile: ArrayList<File>? = null
    var mCurrentPhotoPath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_quotes)

        binding.uploadImage.setOnClickListener(this)


        val text = "<font color=#FF000000>Item will not be packaged.</font> " +
                "<font color=#FFD100>(Please note: This option will void any damage cover for this delivery. Items sent without packaging are at the customers own risk.)  </font>"
        binding.chkTerms2.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)


    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.upload_image -> {
                showPictureDialog()
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

    private fun takePhotoFromCamera() {
        /***************** New implemented  */
        try {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(this@UploadQuotesActivity.packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
                if (photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(
                        this@UploadQuotesActivity,
                        applicationContext.packageName + ".fileprovider", photoFile
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(
                        takePictureIntent,
                        PICK_FROM_CAMERA
                    )
                }
            }
            selectProfileImgDialog!!.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        try {
            intent.putExtra("return-data", true)
            startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                PICK_FROM_GALLERY
            )
            selectProfileImgDialog!!.dismiss()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "request_" + timeStamp + "_"
        val makenewdir = File(Environment.getExternalStorageDirectory(), "zoom2uDir")
        if (!makenewdir.exists()) {
            makenewdir.mkdir()
        }
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".png",  /* suffix */
            makenewdir /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (PICK_FROM_CAMERA == requestCode && count <= 5) {
                val bmOptions = BitmapFactory.Options()
                bmOptions.inJustDecodeBounds = true
                BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
                val photoW = bmOptions.outWidth
                val photoH = bmOptions.outHeight
                // Determine how much to scale down the image
                val scaleFactor: Int =
                    Math.min(
                        photoW / AppUtility.getDeviceWight()!!,
                        photoH / AppUtility.getDeviceHeight()!!
                    )
                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false
                bmOptions.inSampleSize = scaleFactor
                bmOptions.inPurgeable = true
                val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
                arrayOfImageFiles!!.add(mCurrentPhotoPath!!)
                val imagepath = File(mCurrentPhotoPath)
                arrayOfcameraImgFile!!.add(imagepath)
                when (count) {
                    1 -> {
                        if (bitmap != null) binding.imv1.setImageBitmap(bitmap)
                        count++
                    }
                    2 -> {
                        if (bitmap != null) binding.imv2.setImageBitmap(bitmap)
                        count++
                    }
                    3 -> {
                        if (bitmap != null) binding.imv3.setImageBitmap(bitmap)
                        count++
                    }
                    4 -> {
                        if (bitmap != null) binding.imv4.setImageBitmap(bitmap)
                        count++
                    }
                    5 -> {
                        if (bitmap != null) binding.imv5.setImageBitmap(bitmap)
                        count++
                        // btnUploadPhoto.setEnabled(false)
                    }
                }
            } else if (requestCode == PICK_FROM_GALLERY) {
                try {
                    if (resultCode == RESULT_OK && count <= 5) {
                        val selectedImage = data?.data
                        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor =
                            contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                        if (cursor!!.moveToFirst()) {
                            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                            val filePath = cursor.getString(columnIndex)
                            val file = File(filePath)
                            arrayOfImageFiles!!.add(filePath)
                            val mBitmap = BitmapFactory.decodeFile(filePath)
                            when (count) {
                                1 -> {
                                    if (mBitmap != null) binding.imv1.setImageBitmap(mBitmap)
                                    count++
                                }
                                2 -> {
                                    if (mBitmap != null) binding.imv2.setImageBitmap(mBitmap)
                                    count++
                                }
                                3 -> {
                                    if (mBitmap != null) binding.imv3.setImageBitmap(mBitmap)
                                    count++
                                }
                                4 -> {
                                    if (mBitmap != null) binding.imv4.setImageBitmap(mBitmap)
                                    count++
                                }
                                5 -> {
                                    if (mBitmap != null) binding.imv5.setImageBitmap(mBitmap)
                                    count++
                                    //btnUploadPhoto.setEnabled(false)
                                }
                            }
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }



}

