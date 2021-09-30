package com.zoom2u_customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.WindowManager
import com.zoom2u_customer.ui.application.bottom_navigation.base_page.BasePageActivity
import com.zoom2u_customer.ui.splash_screen.LogInSignupMainActivity
import com.zoom2u_customer.utility.AppPreference
import com.zoom2u_customer.utility.AppUtility

class MainActivity : AppCompatActivity() {

    companion object {
        var gcmTokenID: String? = null
        var isMainActivityIsActive = false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh_screen)

        isMainActivityIsActive = true
        AppUtility.fullScreenMode(window)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler(Looper.getMainLooper()).postDelayed({
            if (AppPreference.getSharedPrefInstance().getLoginResponse()!=null) {
                if (TextUtils.isEmpty(AppPreference.getSharedPrefInstance().getLoginResponse()?.access_token)) {
                    val intent = Intent(this@MainActivity, LogInSignupMainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    LoginSuccessFully()
                }
            }else{
                val intent = Intent(this@MainActivity, LogInSignupMainActivity::class.java)
                startActivity(intent)
                finish()
            }

            finish()
        }, 3000)



    }
    private fun LoginSuccessFully() {
        startActivity(
            Intent(this@MainActivity, BasePageActivity:: class.java).setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }




}




