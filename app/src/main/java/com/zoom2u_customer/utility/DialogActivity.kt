package com.zoom2u_customer.utility


import android.app.Activity
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import com.zoom2u_customer.R


class DialogActivity {

    companion object {

        fun alertDialogSingleButton(context: Context?, alertTitle: String, alertMsg: String) {

            val viewGroup = (context as Activity).findViewById<ViewGroup>(R.id.content)

            val dialogView: View =
                LayoutInflater.from(context).inflate(R.layout.dialogview, viewGroup, false)

            val builder = AlertDialog.Builder(context)

            builder.setView(dialogView)

            val alertDialog = builder.create()
            alertDialog.show()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val titleAlert: TextView = dialogView.findViewById(R.id.titleDialog)
            titleAlert.text = alertTitle

            val msgAlertDialog: TextView = dialogView.findViewById(R.id.dialogMessageText)
            msgAlertDialog.text = alertMsg

            val crossBtn: ImageView = dialogView.findViewById(R.id.dialogDoneBtn)
            crossBtn.setOnClickListener {
                alertDialog.dismiss()
            }


            val submitBtn: Button = dialogView.findViewById(R.id.okBtn)
            submitBtn.setOnClickListener {
                alertDialog.dismiss()
            }

        }

        fun logoutDialog(context: Context?, alertTitle: String, alertMsg: String,
                         okText:String?,cancelText:String?,
                         onCancelClick: ()->Unit?,
                         onOkClick: () -> Unit) {
            val viewGroup = (context as Activity).findViewById<ViewGroup>(R.id.content)

            val dialogView: View =
                LayoutInflater.from(context).inflate(R.layout.confrm_dialogview, viewGroup, false)

            val builder = AlertDialog.Builder(context)

            builder.setView(dialogView)

            val alertDialog = builder.create()
            alertDialog.show()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val titleAlert: TextView = dialogView.findViewById(R.id.dialogLogoutTitleText)
            titleAlert.text = alertTitle

            val msgAlertDialog: TextView = dialogView.findViewById(R.id.dialogLogoutMessageText)
            msgAlertDialog.text = alertMsg


            val cancel: TextView = dialogView.findViewById(R.id.cancel)
            cancel.text=cancelText
            cancel.setOnClickListener {
                onCancelClick()
                alertDialog.dismiss()
            }

            val ok: TextView = dialogView.findViewById(R.id.ok)
            ok.text=okText
            ok.setOnClickListener {
                onOkClick()
                alertDialog.dismiss()
            }
            val crossBtn: ImageView = dialogView.findViewById(R.id.dialogDoneBtn)
            crossBtn.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        fun alertDialogOkCallback(context: Context?, alertTitle: String, alertMsg: String,onItemClick: () -> Unit) {

            val viewGroup = (context as Activity).findViewById<ViewGroup>(R.id.content)

            val dialogView: View =
                LayoutInflater.from(context).inflate(R.layout.dialogview, viewGroup, false)

            val builder = AlertDialog.Builder(context)

            builder.setView(dialogView)

            val alertDialog = builder.create()
            alertDialog.show()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val titleAlert: TextView = dialogView.findViewById(R.id.titleDialog)
            titleAlert.text = alertTitle

            val msgAlertDialog: TextView = dialogView.findViewById(R.id.dialogMessageText)
            msgAlertDialog.text = alertMsg

            val crossBtn: ImageView = dialogView.findViewById(R.id.dialogDoneBtn)
            crossBtn.setOnClickListener {
                alertDialog.dismiss()
            }


            val submitBtn: Button = dialogView.findViewById(R.id.okBtn)
            submitBtn.setOnClickListener {
                onItemClick()
                alertDialog.dismiss()
            }

        }




    }
}