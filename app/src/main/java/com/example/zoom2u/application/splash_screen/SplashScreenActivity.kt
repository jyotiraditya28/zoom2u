package com.example.zoom2u.application.splash_screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.zoom2u.R
import com.example.zoom2u.application.ui.details_base_page.BottomNavigationActivity
import com.example.zoom2u.application.ui.details_base_page.base_page.BasePageActivity
import com.example.zoom2u.utility.AppPreference
import com.example.zoom2u.utility.AppUtility

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh_screen)
        AppUtility.fullScreenMode(window)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            if (AppPreference.getSharedPrefInstance().getLoginResponse()!=null) {
                if (TextUtils.isEmpty(AppPreference.getSharedPrefInstance().getLoginResponse()?.access_token)) {
                    val intent = Intent(this@SplashScreenActivity, LogInSignupMainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    LoginSuccessFully()
                }
            }else{
                val intent = Intent(this@SplashScreenActivity, LogInSignupMainActivity::class.java)
                startActivity(intent)
                finish()
            }

            finish()
        }, 3000)



    }
 fun LoginSuccessFully() {
     startActivity(Intent(this@SplashScreenActivity , BottomNavigationActivity:: class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
     finish()
    }




}




