package com.example.zoom2u.application.spalsh_screen

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.zoom2u.R
import com.example.zoom2u.application.ui.details_base_page.BasePageActivity
import com.example.zoom2u.application.ui.log_in.LogInActivity
import com.example.zoom2u.utility.AppPreference

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh_screen)

       /* if (AppPreference.getSharedPrefInstance().getLoginResponse()!=null) {
            if (TextUtils.isEmpty(AppPreference.getSharedPrefInstance().getLoginResponse()?.access_token)) {
                val intent = Intent(this@SplashScreenActivity, LogInActivity::class.java)
                startActivity(intent)
                finish()
            }else {
                LoginSuccessFully()
            }
        }else{*/
            val intent = Intent(this@SplashScreenActivity, LogInActivity::class.java)
            startActivity(intent)
            finish()
        //}

    }
 fun LoginSuccessFully() {
     startActivity(Intent(this@SplashScreenActivity , BasePageActivity:: class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
     finish()
    }
}




