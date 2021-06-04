package com.example.zoom2u.ui.log_in

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.zoom2u.R
import com.example.zoom2u.databinding.ActivityLogInBinding
import com.example.zoom2u.ui.details_base_page.BasePageActivity
import com.example.zoom2u.ui.sign_up.SignUpActivity

class LogInActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)

        binding.loginBtn.setOnClickListener(this)
        binding.register.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.login_btn -> {
                val intent = Intent(this, BasePageActivity::class.java)
                startActivity(intent)
            }
            R.id.register -> {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }
}