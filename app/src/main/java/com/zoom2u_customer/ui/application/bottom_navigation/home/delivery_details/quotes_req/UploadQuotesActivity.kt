package com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details.quotes_req


import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ApiClient.Companion.getServices
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.ActivityUploadQuotesBinding
import com.zoom2u_customer.ui.application.bottom_navigation.home.delivery_details.quotes_req.quote_confirmation_page.QuoteConfirmationActivity
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class UploadQuotesActivity : AppCompatActivity(), View.OnClickListener {
    private var quoteRequestBody: JSONObject? = null
    lateinit var binding: ActivityUploadQuotesBinding
    var selectProfileImgDialog: Dialog? = null
    var count = 1
    var imageClicked=0
    var requestId:Int?=null
    var arrayOfImageFiles: MutableList<String> = ArrayList()
    lateinit var viewModel: UploadQuotesViewModel
    private var repository: UploadQuotesRepository? = null
    private var packagingType:String=""
    private var quoteId:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_quotes)
        AppUtility.hideKeyboardActivityLunched(this)
        AppUtility.hideKeyBoardClickOutside(binding.parentCl, this)
        AppUtility.hideKeyBoardClickOutside(binding.pickupView, this)

        val text = "<font color=#FF000000>Item will not be packaged.</font> " +
                "<font color=#FFD100>(Please note: This option will void any damage cover for this delivery. Items sent without packaging are at the customers own risk.)  </font>"
        binding.thirdPack.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)

        viewModel = ViewModelProvider(this).get(UploadQuotesViewModel::class.java)
        val serviceApi: ServiceApi = getServices()
        repository = UploadQuotesRepository(serviceApi, this)
        viewModel.repository = repository

        if (intent.hasExtra("SaveDeliveryRequestReq")) {
            quoteRequestBody= JSONObject(intent.getStringExtra("SaveDeliveryRequestReq"))


        }


        val text1 =  getString(R.string.term_con1)
        val spannableString = SpannableString(text1)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color=resources.getColor(R.color.black)
                ds.underlineColor=resources.getColor(R.color.black)
                ds.isUnderlineText = true
            }

            override fun onClick(p0: View) {
                try {
                    val browserIntent: Intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.zoom2u.com.au/customer-terms/")
                    )
                    startActivity(browserIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        spannableString.setSpan(clickableSpan, 22, text1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.termsCon.setText(spannableString, TextView.BufferType.SPANNABLE)
        binding.termsCon.movementMethod = LinkMovementMethod.getInstance()



            viewModel.getQuoteSuccess()?.observe(this) {
            if (it != null) {
                if (it.isNotEmpty()) {
                  quoteId=it[it.size-1].toInt()
                    if (it.size > 1) {
                        it.removeAt(it.size-1)
                        viewModel.uploadQuotesImage(quoteId, it)
                    }else if(it.size==1){
                        AppUtility.progressBarDissMiss()
                        val intent = Intent(this, QuoteConfirmationActivity::class.java)
                        intent.putExtra("QuoteId",quoteId.toString())
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                }
            }
        }

        viewModel.finalSuccess.observe(this) {
            if (it != null) {
                AppUtility.progressBarDissMiss()
                val intent = Intent(this, QuoteConfirmationActivity::class.java)
                intent.putExtra("QuoteId",it)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }

        binding.uploadImage.setOnClickListener(this)
        binding.submitQuotesReq.setOnClickListener(this)
        binding.imv1.setOnClickListener(this)
        binding.imv1.isEnabled=false
        binding.imv2.isEnabled=false
        binding.imv3.isEnabled=false
        binding.imv4.isEnabled=false
        binding.imv5.isEnabled=false
        binding.imv2.setOnClickListener(this)
        binding.imv3.setOnClickListener(this)
        binding.imv4.setOnClickListener(this)
        binding.imv5.setOnClickListener(this)



        binding.packagingGroup.setOnCheckedChangeListener { _, checkedId ->
            packagingType = when {
                R.id.first_pack == checkedId -> "House"
                R.id.sec_pack == checkedId -> "Commercial"
                else -> "third"
            }
        }



    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.upload_image -> {
                uploadPhoto()
            }
            R.id.submit_quotes_req -> {
                if (checkValidation(binding.notes.text.toString(), packagingType)) {

                    DialogActivity.logoutDialog(
                        this,
                        "Confirm!",
                        "Due to the nature of this delivery, it will be created as a quote request and sent out to drivers for bidding on, instead of being a fixed quote. Our drivers will start providing quotes for this delivery, which you can view and accept. Do you wish to continue?",
                        "Yes","No",
                        onCancelClick=::onNoClick,
                        onOkClick = ::onYesClick
                    )

                }
            }
            R.id.imv1 -> {
                uploadPhoto()
                imageClicked=1
            }
            R.id.imv2 -> {
                uploadPhoto()
                imageClicked=2
            }
            R.id.imv3 -> {
                uploadPhoto()
                imageClicked=3
            }
            R.id.imv4 -> {
                uploadPhoto()
                imageClicked=4
            }
            R.id.imv5 -> {
                uploadPhoto()
                imageClicked=5
            }

        }
    }

    private fun onNoClick(){

    }
    private fun onYesClick(){
        quoteRequestBody?.getJSONObject("_requestModel")
            ?.put("Notes", binding.notes.text.trim().toString().trim())
        viewModel.getQuoteRequest(quoteRequestBody, arrayOfImageFiles)
    }
    private fun checkValidation(desc: String, packaging_Type: String): Boolean {
        if (desc == "") {
            DialogActivity.alertDialogSingleButton(this, "Awaiting!", "Please enter your parcel description.")
            AppUtility.validateEditTextField(
                binding.notes,
                "Please enter your parcel description."
            )
            return false
        }
       else if (packaging_Type.isNullOrEmpty()) {
            DialogActivity.alertDialogSingleButton(this, "Awaiting!", "Please select a packaging type.")
            return false
        }
        else if(!binding.chkTerms.isChecked){
            Toast.makeText(this,"Please Accept the customer Terms and Conditions.", Toast.LENGTH_LONG).show()
            return false
        }
     return true
    }

    private fun uploadPhoto(){
        CropImage.activity().start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                if (imageClicked == 0) {
                    when (count) {
                        1 -> {
                            Picasso.with(this).load(resultUri).into(binding.imv1)
                            binding.imv1.isEnabled = true
                            count++
                            arrayOfImageFiles.add( resultUri.path.toString())
                        }
                        2 -> {
                            Picasso.with(this).load(resultUri).into(binding.imv2)
                            count++
                            binding.imv2.isEnabled = true
                            arrayOfImageFiles.add( resultUri.path.toString())
                        }
                        3 -> {
                            Picasso.with(this).load(resultUri).into(binding.imv3)
                            count++
                            binding.imv3.isEnabled = true
                            arrayOfImageFiles.add( resultUri.path.toString())
                        }
                        4 -> {
                            Picasso.with(this).load(resultUri).into(binding.imv4)
                            count++
                            binding.imv4.isEnabled = true
                            arrayOfImageFiles.add( resultUri.path.toString())
                        }
                        5 -> {
                            Picasso.with(this).load(resultUri).into(binding.imv5)
                            count++
                            binding.imv5.isEnabled = true
                            arrayOfImageFiles.add( resultUri.path.toString())
                            binding.uploadImage.isClickable = false
                        }
                    }
                } else {
                    when (imageClicked) {
                        1 -> {
                            Picasso.with(this).load(resultUri).into(binding.imv1)
                            arrayOfImageFiles.add(1, resultUri.path.toString())
                            imageClicked=0
                        }
                        2 -> {
                            Picasso.with(this).load(resultUri).into(binding.imv2)
                            arrayOfImageFiles.add(2, resultUri.path.toString())
                            imageClicked=0
                        }
                        3 -> {
                            Picasso.with(this).load(resultUri).into(binding.imv3)
                            arrayOfImageFiles.add(3, resultUri.path.toString())
                            imageClicked=0
                        }
                        4 -> {
                            Picasso.with(this).load(resultUri).into(binding.imv4)
                            arrayOfImageFiles.add(4, resultUri.path.toString())
                            imageClicked=0
                        }
                        5 -> {
                            Picasso.with(this).load(resultUri).into(binding.imv5)
                            arrayOfImageFiles.add(5, resultUri.path.toString())
                            imageClicked=0
                        }

                    }
                }
            }
        }

    }



}

