package com.zoom2u_customer.ui.application.bottom_navigation.base_page

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ApiClient.Companion.getServices
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.ActivityBasepageBinding
import com.zoom2u_customer.ui.application.bottom_navigation.profile.ProfileRepository
import com.zoom2u_customer.ui.application.get_location.GetLocationClass
import com.zoom2u_customer.utility.AppUtility
import com.zoom2u_customer.utility.DialogActivity

class BasePageActivity : AppCompatActivity(),  BottomNavigationView.OnNavigationItemSelectedListener {
    val APP_PERMISSION_REQUEST_CODE = 1010
    lateinit var viewModel: BasePageViewModel
    private var repository: BasePageRepository? = null
    private var profileRepository:ProfileRepository?=null
    lateinit var binding: ActivityBasepageBinding
    private lateinit var mainPagerAdapter: MainPagerAdapter
    var mAuth_Firebase: FirebaseAuth? = null
    var firebase_CurrentUser: FirebaseUser? = null
    private var getLocationClass: GetLocationClass? = null
    /** get gcm token id*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_basepage)

        viewModel = ViewModelProvider(this).get(BasePageViewModel::class.java)
        val serviceApi: ServiceApi = getServices()
        repository = BasePageRepository(serviceApi, this)
        profileRepository = ProfileRepository(serviceApi,this)
        viewModel.repository = repository
        viewModel.profileRepository = profileRepository
        viewModel.getProfile()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            val token = task.result
            viewModel.sendDeviceTokenIDWithOutLocation(token)

        })



        getLocationClass = GetLocationClass(this)
      //  getLocationClass?.getCurrentLocation(onAddress = ::getAddress)
         checkToEnableAppPermissions()

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

    private fun getAddress(lat: Double, lang: Double) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                //Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            viewModel.sendDeviceTokenID(lat,lang,token)

        })



    }
    override fun onStart() {
        super.onStart()
        //Batch.onStart(this)
        // Check if user is signed in (non-null) and update UI accordingly.
        firebase_CurrentUser = mAuth_Firebase?.currentUser
        Log.i("", "---  Current firebase user --- $firebase_CurrentUser")
    }
    fun checkToEnableAppPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            val permission = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
            )
            if (ContextCompat.checkSelfPermission(
                    this@BasePageActivity,
                    permission[0]
                ) === PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(
                    this@BasePageActivity,
                    permission[1]
                ) === PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(
                    this@BasePageActivity,
                    permission[2]
                ) === PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(
                    this@BasePageActivity,
                    permission[3]
                ) === PackageManager.PERMISSION_DENIED
            ) {
                val alertDialog = AlertDialog.Builder(this@BasePageActivity)
                alertDialog.setTitle("Permission required!")
                alertDialog.setMessage(
                    "Zoom2u is a location based application" +
                            " based on location services where we need to accessyour location" +
                            " and access to your images for picture post and profile picture upload."
                )
                alertDialog.setPositiveButton(
                    "Okay"
                ) { dialog, _ ->
                    ActivityCompat.requestPermissions(
                        this@BasePageActivity , arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        ),
                        APP_PERMISSION_REQUEST_CODE
                    )
                    dialog.dismiss()
                }
                alertDialog.show()
            } else
                getLocationClass?.getCurrentLocation(onAddress = ::getAddress)
        } else
            getLocationClass?.getCurrentLocation(onAddress = ::getAddress)
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
        DialogActivity.logoutDialog(
            this,
            "Are you sure!",
            "Are you want Logout?",
            "Ok","Cancel",
            onCancelClick=::onCancelClick,
            onOkClick = ::onOkClick
        )
    }
    private fun onCancelClick(){

    }

    private fun onOkClick() {
        AppUtility.onLogoutCall(this)
    }
}