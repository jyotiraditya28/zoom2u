package com.example.zoom2u.ui.sign_up

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.zoom2u.R
import com.example.zoom2u.databinding.ActivitySignUpBinding
import com.example.zoom2u.ui.details_base_page.BasePageActivity
import com.example.zoom2u.ui.log_in.LogInActivity

class SignUpActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        binding.signupBtn.setOnClickListener(this)
        binding.signin.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.signup_btn -> {
                val intent = Intent(this, BasePageActivity::class.java)
                startActivity(intent)
            }
            R.id.signin -> {
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
            }

        }
    }
}