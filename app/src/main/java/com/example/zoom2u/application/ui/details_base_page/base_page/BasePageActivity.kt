package com.example.zoom2u.application.ui.details_base_page.base_page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.example.zoom2u.R
import com.example.zoom2u.application.splash_screen.LogInSignupMainActivity

import com.example.zoom2u.application.ui.log_in.LoginResponce
import com.example.zoom2u.databinding.ActivityBasepageBinding
import com.example.zoom2u.utility.AppPreference
import com.example.zoom2u.utility.DialogActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class BasePageActivity : AppCompatActivity(),  BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var binding:ActivityBasepageBinding
    private lateinit var mainPagerAdapter: MainPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_basepage)

        mainPagerAdapter = MainPagerAdapter(supportFragmentManager)

        mainPagerAdapter.setItems(arrayListOf(
            MainScreen.LOGS,
            MainScreen.PROGRESS,
            MainScreen.PROFILE,
            MainScreen.WORK
        ))

        val defaultScreen = MainScreen.LOGS
        scrollToScreen(defaultScreen)
        selectBottomNavigationViewMenuItem(defaultScreen.menuItemId)
        supportActionBar?.setTitle(defaultScreen.titleStringId)

        binding.navigation.setOnNavigationItemSelectedListener(this)

       binding.viewPager.adapter = mainPagerAdapter
       binding.viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                val selectedScreen = mainPagerAdapter.getItems()[position]
                selectBottomNavigationViewMenuItem(selectedScreen.menuItemId)
                supportActionBar?.setTitle(selectedScreen.titleStringId)
            }
        })
    }



    private fun scrollToScreen(mainScreen: MainScreen) {
        val screenPosition = mainPagerAdapter.getItems().indexOf(mainScreen)
        if (screenPosition != binding.viewPager.currentItem) {
            binding.viewPager.currentItem = screenPosition
        }
    }


    private fun selectBottomNavigationViewMenuItem(@IdRes menuItemId: Int) {
        binding.navigation.setOnNavigationItemSelectedListener(null)
        binding.navigation.selectedItemId = menuItemId
        binding.navigation.setOnNavigationItemSelectedListener(this)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        getMainScreenForMenuItem(item.itemId)?.let {
            scrollToScreen(it)
            supportActionBar?.setTitle(it.titleStringId)
            return true
        }
        return false

    }


    override fun onBackPressed() {
        DialogActivity.alertDialogDoubleButton(this, "Are you sure!", "Are you want Logout?", onItemClick = ::onItemClick)

    }
    private fun onItemClick() {
        val loginResponce: LoginResponce? = AppPreference.getSharedPrefInstance().getLoginResponse()
        loginResponce?.access_token = ""
        AppPreference.getSharedPrefInstance().setLoginResponse(Gson().toJson(loginResponce))

        val intent = Intent(this, LogInSignupMainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

}