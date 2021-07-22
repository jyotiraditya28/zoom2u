package com.zoom2u_customer.ui.application.bottom_navigation.base_page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.zoom2u_customer.R
import com.zoom2u_customer.ui.splash_screen.LogInSignupMainActivity
import com.zoom2u_customer.ui.log_in.LoginResponce
import com.zoom2u_customer.databinding.ActivityBasepageBinding
import com.zoom2u_customer.utility.AppPreference
import com.zoom2u_customer.utility.DialogActivity

class BasePageActivity : AppCompatActivity(),  BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityBasepageBinding
    private lateinit var mainPagerAdapter: MainPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_basepage)

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
        binding.navigation.itemIconTintList = null;
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
        DialogActivity.logoutDialog(this, "Are you sure!", "Are you want Logout?",
            "Ok","Cancel",
            onCancelClick=::onCancelClick,
            onOkClick = ::onOkClick)
    }
    private fun onCancelClick(){}

    private fun onOkClick() {
        val loginResponce: LoginResponce? = AppPreference.getSharedPrefInstance().getLoginResponse()
        loginResponce?.access_token = ""
        AppPreference.getSharedPrefInstance().setLoginResponse(Gson().toJson(loginResponce))

        val intent = Intent(this, LogInSignupMainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

}