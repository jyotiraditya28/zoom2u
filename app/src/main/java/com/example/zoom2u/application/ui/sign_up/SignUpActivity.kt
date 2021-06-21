package com.example.zoom2u.application.ui.sign_up

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.zoom2u.R
import com.example.zoom2u.apiclient.ApiClient
import com.example.zoom2u.apiclient.ServiceApi

import com.example.zoom2u.application.ui.details_base_page.BasePageActivity
import com.example.zoom2u.application.ui.log_in.*
import com.example.zoom2u.databinding.ActivitySignUpBinding
import com.example.zoom2u.utility.AppUtility
import com.example.zoom2u.utility.DialogActivity


class SignUpActivity : AppCompatActivity(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {
    lateinit var binding: ActivitySignUpBinding
    var country = arrayOf("India", "USA", "China", "Japan", "Other")

    lateinit var viewModel: SignUpViewModel
    private var repository: SignUpRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        binding.signupBtn.setOnClickListener(this)
        binding.signin.setOnClickListener(this)
        binding.findUs.setOnClickListener(this)
        // setSpinnerData()

        viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        val serviceApi: ServiceApi = ApiClient.getServices()
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

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.signup_btn -> {
                setSignUpdata()
            }
            R.id.signin -> {
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
            }
            R.id.find_us -> {
                // binding.findUs.showDropDown()
            }

        }
    }

    private fun setSignUpdata() {
        if(checkValidition(binding.firstName.text.toString(), binding.lastName.text.toString(), binding.company.text.toString(), binding.email.text.toString(), binding.phone.text.toString(),binding.findUs.text.toString(), binding.pass.text.toString(), binding.confirmPass.text.toString()))
        viewModel.getSignUp(SignUpRequest(binding.firstName.text.toString(), binding.lastName.text.toString(), binding.company.text.toString(), binding.email.text.toString(), binding.phone.text.toString(), binding.findUs.text.toString(), binding.pass.text.toString(), binding.confirmPass.text.toString(), "true", "0", "Deliveries"))
    }


    private fun checkValidition(first_name: String, last_name: String, company: String, email: String, phone: String, findUs: String, pass: String, confirmPass: String
    ): Boolean {
        if (first_name.equals("")) {
            DialogActivity.alertDialogView(this, "Alert!", "Please enter your first name")
            AppUtility.validateTextField(binding.firstName)
            return false
        } else if (!first_name.matches(("[a-zA-Z ]+").toRegex())) {
            DialogActivity.alertDialogView(this, "Alert!", "Please enter alphabets in first name")
            AppUtility.validateTextField(binding.firstName)
            return false
        } else if (last_name.equals("")) {
            DialogActivity.alertDialogView(this, "Alert!", "Please enter your last name")
            AppUtility.validateTextField(binding.lastName)
            return false
        } else if (!last_name.matches(("[a-zA-Z ]+").toRegex())) {
            DialogActivity.alertDialogView(this, "Alert!", "Please enter alphabets in last name")
            AppUtility.validateTextField(binding.lastName)
            return false
        } else if (company.equals("")) {
            DialogActivity.alertDialogView(this, "Alert!", "Please enter your company name")
            AppUtility.validateTextField(binding.company)
            return false
        } else if (!company.matches(("[a-zA-Z ]+").toRegex())) {
            DialogActivity.alertDialogView(this, "Alert!", "Please enter alphabets in company name")
            AppUtility.validateTextField(binding.company)
            return false
        } else if (email.equals("")) {
            DialogActivity.alertDialogView(this, "Alert!", "Please enter your email")
            AppUtility.validateTextField(binding.email)
            return false
        } else if (!email.matches(("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").toRegex())) {
            DialogActivity.alertDialogView(this, "Alert!", "Please enter vaild email")
            AppUtility.validateTextField(binding.email)
            return false
        } else if (phone.equals("")) {
            DialogActivity.alertDialogView(this, "Alert!", "Please enter your phone number")
            AppUtility.validateTextField(binding.phone)
            return false
        } else if (!phone.matches(("^[\\s0-9\\()\\-\\+]+$").toRegex())) {
            DialogActivity.alertDialogView(this, "Alert!", "Please enter valid phone number")
            AppUtility.validateTextField(binding.phone)
            return false
        } else if (pass.equals("")) {
            DialogActivity.alertDialogView(this, "Alert!", "Password must be at least 6 characters long")
            AppUtility.validateTextField(binding.pass)
            return false
        } else if (pass.length < 6) {
            DialogActivity.alertDialogView(this, "Alert!", "Password must be at least 6 characters long")
            AppUtility.validateTextField(binding.pass)
            return false
        } else if (confirmPass.equals("")) {
            DialogActivity.alertDialogView(this, "Alert!", "Please enter confirm password")
            AppUtility.validateTextField(binding.confirmPass)
            return false
        } else if (!confirmPass.equals(pass)) {
            DialogActivity.alertDialogView(this, "Alert!", "Password confirmation and Password must match.")
            AppUtility . validateTextField (binding.pass)
            AppUtility . validateTextField (binding.confirmPass)
            return false
        }
        return true
    }

    /*  fun setSpinnerData(){
          var adapter: ArrayAdapter<String> = ArrayAdapter<String>(this,
              android.R.layout.select_dialog_item, country)
          binding.findUs.setAdapter(adapter)

      }*/

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text: String = parent?.getItemAtPosition(position).toString()
        binding.findUs.setText(text)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}