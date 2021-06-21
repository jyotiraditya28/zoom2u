package com.example.zoom2u.utility

import android.app.ProgressDialog
import android.content.Context
import android.util.TypedValue
import android.view.WindowManager
import android.widget.ListPopupWindow
import android.widget.Spinner
import com.example.zoom2u.R
import com.example.zoom2u.application.ui.log_in.LoginResponce
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.*

class AppUtility {
    companion object {

        var progressDialog :ProgressDialog?=null
        fun getJsonObject(params: String?): JsonObject {
            val parser = JsonParser()
            return parser.parse(params).asJsonObject
        }


        var emailPattern = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")


        fun validateTextField(validateTxtField: TextInputEditText) {
            validateTxtField.setBackgroundResource(R.drawable.blankbox)
        }

        fun spinnerPopUpWindow(spinner: Spinner?,context: Context) {
            try {
                val popup = Spinner::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val popupWindow = popup[spinner] as ListPopupWindow
                popupWindow.height = getintToPX(140,context)

                popupWindow.setBackgroundDrawable(context.getResources().getDrawable(android.R.drawable.dialog_holo_light_frame))
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


        fun getintToPX(i: Int,context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                i.toFloat(),
                context.getResources()?.getDisplayMetrics()
            )
                .toInt()
        }


        fun progressBarShow(context: Context?) {
             progressDialog = ProgressDialog(context, R.style.progressbarstyle)
            progressDialog?.setMessage(
                "Loding"+ "..."
            )
            progressDialog?.setCancelable(false)
            progressDialog?.setCanceledOnTouchOutside(false)
            progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog?.setIndeterminate(true)
            progressDialog?.show()
        }


        fun progressBarDissMiss() {
            try {
                progressDialog?.dismiss()
                progressDialog?.setCancelable(false)
                progressDialog?.getWindow()
                    ?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        fun getApiHeaders(): Map<String, String> {
            val headers: MutableMap<String, String> = HashMap()
            val loginResponce :LoginResponce?=AppPreference.getSharedPrefInstance().getLoginResponse()
            headers.put("authorization" ,"Bearer"+" "+loginResponce?.access_token)
            return headers
        }


    }
}