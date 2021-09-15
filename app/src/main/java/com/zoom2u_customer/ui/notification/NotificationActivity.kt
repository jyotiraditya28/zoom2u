package com.zoom2u_customer.ui.notification

import android.app.Activity
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.zoom2u_customer.MainActivity
import com.zoom2u_customer.R
import com.zoom2u_customer.apiclient.ApiClient
import com.zoom2u_customer.apiclient.ServiceApi
import com.zoom2u_customer.databinding.ActivityNotificationBinding
import com.zoom2u_customer.services.MyFcmListenerService
import com.zoom2u_customer.ui.application.bottom_navigation.base_page.BasePageActivity

class NotificationActivity : AppCompatActivity() {
    lateinit var binding: ActivityNotificationBinding
    var msgstr = ""
    var isChatMsg = ""

    lateinit var viewModel: NotificationViewModel
    private var repository: NotificationRepository? = null
    var bookingIDOfNotification = "0"
    var notificationUIVisibilityCount = 0
    private fun showNotificationDialog() {
        val viewGroup = (this as Activity).findViewById<ViewGroup>(R.id.content)

        val dialogView: View =
            LayoutInflater.from(this).inflate(R.layout.default_notificationview, viewGroup, false)

        val builder = AlertDialog.Builder(this)

        builder.setView(dialogView)

        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val titleAlert: TextView = dialogView.findViewById(R.id.titleDialog)
        titleAlert.text = "Booking Status!"

        val msgAlertDialog: TextView = dialogView.findViewById(R.id.dialogMessageText)
        msgAlertDialog.text = msgstr

        val crossBtn: ImageView = dialogView.findViewById(R.id.dialogDoneBtn)
        crossBtn.setOnClickListener {
            alertDialog.dismiss()
        }



    }


    @Synchronized
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        try {
            try {
                if (getIntent().getStringExtra("bookingId") != null)
                    bookingIDOfNotification = intent.getStringExtra("bookingId").toString()
                else if (!MyFcmListenerService.prefrenceForGCM?.getString(
                        "bookingId",
                        "0"
                    ).equals("0")
                ) bookingIDOfNotification =
                    MyFcmListenerService.prefrenceForGCM?.getString("bookingId", "0").toString()
            } catch (e: Exception) {
                e.printStackTrace()
                bookingIDOfNotification = "0"
            }
            if (bookingIDOfNotification == "0") {
                if (isChatMsg != "")
                    finish()
                else {
                    msgstr = getIntent().getStringExtra("message")!!
                    if (msgstr != "")
                        showNotificationDialog()
                    else finish()
              }
            } else {
               notificationUIVisibilityCount = 1
                initView()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification)


      viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
      val serviceApi: ServiceApi = ApiClient.getServices()
      repository = NotificationRepository(serviceApi, this)
      viewModel.repository = repository

        try {
            if (!BasePageActivity.isMainActivityIsActive) {
                launchMainActivityOnAppKilled()
            } else {
                try {
                    if (intent.getStringExtra("bookingId") != null)
                        bookingIDOfNotification =
                        intent.getStringExtra("bookingId")!!
                    else if (!MyFcmListenerService.prefrenceForGCM!!.getString(
                            "bookingId",
                            "0"
                        ).equals("0")
                    ) bookingIDOfNotification =
                        MyFcmListenerService.prefrenceForGCM!!.getString("bookingId", "0")!!
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    bookingIDOfNotification = "0"
                }
                try {
                    if (intent.getStringExtra("chat") != null)
                        isChatMsg = intent.getStringExtra("chat").toString()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    isChatMsg = ""
                }
                clearNotificationFromNotificationBar()
                if (bookingIDOfNotification == "0") {
                    if (isChatMsg != "")
                        finish()
                    else {
                        msgstr = intent.getStringExtra("message")!!
                        if (msgstr != "")
                            showNotificationDialog()
                        else
                            finish()
                    }
                } else {
                    super.setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                    setContentView(R.layout.activity_notification)
                    val policy = ThreadPolicy.Builder().build()
                    StrictMode.setThreadPolicy(policy)
                    try {
                        notificationUIVisibilityCount = 1


                        binding.closeNotificatinView.setOnClickListener(View.OnClickListener {
                            notificationUIVisibilityCount = 0
                            finish()
                        })

                        binding.thumabsUpBtn.setOnClickListener {
                            try {

                                viewModel.callServiceToRateCourier(bookingIDOfNotification.toInt() ,1)
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }
                        binding.thumabsDownBtn.setOnClickListener {
                            try {

                                viewModel.callServiceToRateCourier(bookingIDOfNotification.toInt() ,-1)
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }

                        initView()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            finish()
        }

    }

    @Synchronized
    fun initView() {

         viewModel.getRateDetails(bookingIDOfNotification.toInt())

    }


        private fun launchMainActivityOnAppKilled() {
            val launchMainActivity = Intent("android.intent.action.MAIN")
            launchMainActivity.setClass(this@NotificationActivity, MainActivity::class.java)
            launchMainActivity.action = Intent.ACTION_MAIN
            launchMainActivity.addCategory(Intent.CATEGORY_LAUNCHER)
            if (intent.getStringExtra("bookingId") != null) {
                launchMainActivity.putExtra("message", intent.getStringExtra("message"))
                if (MyFcmListenerService.prefrenceForGCM == null) MyFcmListenerService.prefrenceForGCM =
                    getSharedPreferences("bookingId", 0)
                if (MyFcmListenerService.loginEditorForGCM == null) MyFcmListenerService.loginEditorForGCM =
                    MyFcmListenerService.prefrenceForGCM?.edit()
                MyFcmListenerService.loginEditorForGCM?.putString(
                    "bookingId",
                    intent.getStringExtra("bookingId")
                )
                MyFcmListenerService.loginEditorForGCM?.commit()
            } else {
                launchMainActivity.putExtra("message", intent.getStringExtra("message"))
            }
            if (intent.getStringExtra("chat") != null) launchMainActivity.putExtra(
                "chat",
                intent.getStringExtra("chat")
            ) else launchMainActivity.putExtra("chat", intent.getStringExtra("chat"))
            startActivity(launchMainActivity)
            finish()
            clearNotificationFromNotificationBar()
        }

    private fun clearNotificationFromNotificationBar() {
        var notificationManager: NotificationManager? =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager!!.cancel(0)
        notificationManager = null
    }
}