package com.zoom2u_customer.application.ui.sign_up

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.application.ui.log_in.LogInActivity
import com.zoom2u_customer.databinding.ActivitySignUpBinding
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity
import com.zoom2u_customer.utility.utility_custom_class.MySpinnerAdapter
import java.lang.reflect.Field


class SignUpActivity : AppCompatActivity(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {
    lateinit var binding: ActivitySignUpBinding
    private var categories: MutableList<String> = mutableListOf()
    lateinit var viewModel: SignUpViewModel
    private var repository: SignUpRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        AppUtility.hideKeyboard(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        binding.signupBtn.setOnClickListener(this)
        binding.signin.setOnClickListener(this)
        binding.findUs.setOnClickListener(this)

        binding.spinner.onItemSelectedListener = this

        categories.add("Google")
        categories.add("Radio Ad")
        categories.add("Newspaper")
        categories.add("Received a delivery")
        categories.add("Referral")
        categories.add("Facebook")
        categories.add("Linkedin")
        categories.add("Twitter")
        categories.add("Billboard")
        categories.add("Other")



        binding.spinner.adapter = MySpinnerAdapter(this, categories)
        //AppUtility.spinnerPopUpWindow(binding.spinner)

        try {
            val popup: Field = Spinner::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val popupWindow = popup.get(binding.spinner) as ListPopupWindow
            popupWindow.listView?.isScrollbarFadingEnabled = false
            popupWindow.height=getIntToPX(30)
        } catch (e: NoClassDefFoundError) {

        } catch (e: ClassCastException) {
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
        }


        viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        val serviceApi: ServiceApi = com.zoom2u_customer.apiclient.ApiClient.getServices()
        repository = SignUpRepository(serviceApi, this,onResponseCallback = ::onResponseCallback)
        viewModel.repository = repository

    }

    private fun onResponseCallback(success: String) {
        AppUtility.progressBarDissMiss()
        Toast.makeText(this, "You have been successfully registered and logged in.", Toast.LENGTH_LONG).show()
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun getIntToPX(i: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            i.toFloat(),
            this.resources.displayMetrics
        )
            .toInt()
    }
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.signup_btn -> {
                setSignUpData()
            }
            R.id.signin -> {
                val intent = Intent(this, LogInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }
            R.id.find_us -> {
               binding.spinner.performClick()
            }

        }
    }

    private fun setSignUpData() {
        if(checkValidation(binding.firstName.text.toString(), binding.lastName.text.toString(), binding.company.text.toString(), binding.email.text.toString(), binding.phone.text.toString(),binding.findUs.text.toString(), binding.pass.text.toString(), binding.confirmPass.text.toString(),binding.chkTerms.isChecked))
        viewModel.getSignUp(SignUpRequest(binding.firstName.text.toString(), binding.lastName.text.toString(), binding.company.text.toString(), binding.email.text.toString(), binding.phone.text.toString(), binding.findUs.text.toString(), binding.pass.text.toString(), binding.confirmPass.text.toString(), "true", "0", "Deliveries"))
    }


    private fun checkValidation(first_name: String, last_name: String, company: String, email: String, phone: String, findUs: String, pass: String, confirmPass: String, isChecked:Boolean): Boolean {
        if (first_name == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your first name")
            AppUtility.validateTextField(binding.firstName)
            return false
        } else if (!first_name.matches(("[a-zA-Z ]+").toRegex())) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter alphabets in first name")
            AppUtility.validateTextField(binding.firstName)
            return false
        } else if (last_name == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your last name")
            AppUtility.validateTextField(binding.lastName)
            return false
        } else if (!last_name.matches(("[a-zA-Z ]+").toRegex())) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter alphabets in last name")
            AppUtility.validateTextField(binding.lastName)
            return false
        } else if (company == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your company name")
            AppUtility.validateTextField(binding.company)
            return false
        } else if (!company.matches(("[a-zA-Z ]+").toRegex())) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter alphabets in company name")
            AppUtility.validateTextField(binding.company)
            return false
        } else if (email == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your email")
            AppUtility.validateTextField(binding.email)
            return false
        } else if (!email.matches(("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").toRegex())) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter vaild email")
            AppUtility.validateTextField(binding.email)
            return false
        } else if (phone == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter your phone number")
            AppUtility.validateTextField(binding.phone)
            return false
        } else if (!phone.matches(("^[\\s0-9\\()\\-\\+]+$").toRegex())) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter valid phone number")
            AppUtility.validateTextField(binding.phone)
            return false
        } else if (pass == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Password must be at least 6 characters long")
            AppUtility.validateTextField(binding.pass)
            return false
        } else if (pass.length < 6) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Password must be at least 6 characters long")
            AppUtility.validateTextField(binding.pass)
            return false
        } else if (confirmPass == "") {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Please enter confirm password")
            AppUtility.validateTextField(binding.confirmPass)
            return false
        } else if (confirmPass != pass) {
            DialogActivity.alertDialogSingleButton(this, "Alert!", "Password confirmation and Password must match.")
            AppUtility . validateTextField (binding.pass)
            AppUtility . validateTextField (binding.confirmPass)
            return false
        }else if(!isChecked){
            //DialogActivity.alertDialogSingleButton(this, "Alert!", "Please Accept the customer Terms and Conditions.")
            Toast.makeText(this,"Please Accept the customer Terms and Conditions.",Toast.LENGTH_LONG).show()
            return false
        }
      return true
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedText = categories[position]
        binding.findUs.setText(selectedText)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}