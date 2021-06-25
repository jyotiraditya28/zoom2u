package com.example.zoom2u.application.splash_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.zoom2u.R
import com.example.zoom2u.application.ui.log_in.LogInActivity
import com.example.zoom2u.application.ui.sign_up.SignUpActivity
import com.example.zoom2u.databinding.ActivityLogInSignupMainBinding
import com.example.zoom2u.utility.AppUtility

class LogInSignupMainActivity : AppCompatActivity(), View.OnClickListener  {
    lateinit var binding: ActivityLogInSignupMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppUtility.fullScreenMode(window)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in_signup_main)
        binding.loginWithEmailBtn.setOnClickListener(this)
        binding.signUpBtn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.loginWithEmailBtn -> {
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
            }
            R.id.signUpBtn -> {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }
}