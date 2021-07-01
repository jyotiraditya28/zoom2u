package com.zoom2u_customer.utility

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ListPopupWindow
import android.widget.Spinner
import androidx.core.app.ActivityCompat
import com.google.android.material.textfield.TextInputEditText

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.zoom2u_customer.R
import com.zoom2u_customer.ui.log_in.LoginResponce
import java.util.*


class AppUtility {
    companion object {

        var progressDialog: ProgressDialog? = null


        var emailPattern = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")


        fun validateTextField(validateTxtField: TextInputEditText) {
            validateTxtField.setBackgroundResource(R.drawable.blankbox)
        }

        fun spinnerPopUpWindow(spinner: Spinner?) {
            try {
                val popup = Spinner::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val popupWindow = popup[spinner] as ListPopupWindow
                popupWindow.height = getintToPX(140)

                popupWindow.setBackgroundDrawable(
                    Zoom2u.getInstance()?.resources?.getDrawable(android.R.drawable.dialog_holo_light_frame)
                )
            } catch (e: NoClassDefFoundError) {
                e.message
            } catch (e: ClassCastException) {
                e.message
            } catch (e: NoSuchFieldException) {
                e.message
            } catch (e: IllegalAccessException) {
                e.message
            }

        }


        fun getintToPX(i: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                i.toFloat(),
                Zoom2u.getInstance()?.resources?.displayMetrics
            )
                .toInt()
        }


        fun progressBarShow(context: Context?) {
            progressDialog = ProgressDialog(context as Activity, R.style.progressbarstyle)
            progressDialog?.setMessage(
                "Loading" + "..."
            )
            progressDialog?.setCancelable(false)
            progressDialog?.setCanceledOnTouchOutside(false)
            progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog?.isIndeterminate = true
            progressDialog?.show()
        }


        fun progressBarDissMiss() {
            try {
                progressDialog?.dismiss()
                progressDialog?.setCancelable(false)
                progressDialog?.window
                    ?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        fun getApiHeaders(): Map<String, String> {
            val headers: MutableMap<String, String> = HashMap()
            val loginResponce: LoginResponce? =
                AppPreference.getSharedPrefInstance().getLoginResponse()
            headers.put("authorization", "Bearer" + " " + loginResponce?.access_token)
            return headers
        }

        fun askCameraTakePicture(context: Context?): Boolean {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                if (verifyPermissions(Zoom2uContractProvider.cameraPermissions)) {
                    return true
                } else {
                    ActivityCompat.requestPermissions(
                        (context as Activity?)!!,
                        Zoom2uContractProvider.cameraPermissions,
                        1
                    )
                }
            } else {
                return true
            }
            return false
        }


        fun askGalleryTakeImagePermission(context: Context?): Boolean {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                if (verifyPermissions(Zoom2uContractProvider.galleryPermissions)) {
                    return true
                } else {
                    ActivityCompat.requestPermissions(
                        (context as Activity?)!!,
                        Zoom2uContractProvider.galleryPermissions,
                        1
                    )
                }
            } else {
                return true
            }
            return false
        }

        fun verifyPermissions(grantResults: Array<String>): Boolean {
            for (result in grantResults) {
                if (ActivityCompat.checkSelfPermission(
                        Zoom2u.getInstance()!!,
                        result
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }

        fun isInternetConnected(): Boolean {
            val cm = Zoom2u.getInstance()
                ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        fun fullScreenMode(window: Window) {
            val currentApiVersion = Build.VERSION.SDK_INT

            val flags: Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            if (currentApiVersion >= Build.VERSION_CODES.KITKAT)
                window.decorView.systemUiVisibility = flags
        }


        fun getJsonObject(params: String?): JsonObject? {
            val parser = JsonParser()
            return parser.parse(params).asJsonObject
        }

        fun hideKeyboard(activity:Activity) {
           activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        fun hideKeyboard(context: Context) {
            val inputManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val v = (context as Activity).currentFocus ?: return
            inputManager.hideSoftInputFromWindow(v.windowToken, 0)
        }

        fun validateEditTextField(validateTxtField: EditText, msgStr: String?) {
            validateTxtField.hint = msgStr
            validateTxtField.setHintTextColor(Color.parseColor("#FF476A"))

        }
    }
}