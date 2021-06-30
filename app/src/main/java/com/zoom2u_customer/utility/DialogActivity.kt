package com.zoom2u_customer.utility


import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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


            val titlealert: TextView = dialogView.findViewById(R.id.titleDialog)
            titlealert.text = alertTitle

            val msgAlertDialog: TextView = dialogView.findViewById(R.id.dialogMessageText)
            msgAlertDialog.text = alertMsg

            val crrosbtn: ImageView = dialogView.findViewById(R.id.dialogDoneBtn)
            crrosbtn.setOnClickListener {
                alertDialog.dismiss()
            }


            val submitbtn: Button = dialogView.findViewById(R.id.okBtn)
            submitbtn.setOnClickListener {
                alertDialog.dismiss()
            }

        }

        fun alertDialogDoubleButton(context: Context?, alertTitle: String, alertMsg: String, onItemClick: () -> Unit) {
            val viewGroup = (context as Activity).findViewById<ViewGroup>(R.id.content)

            val dialogView: View =
                LayoutInflater.from(context).inflate(R.layout.confrm_dialogview, viewGroup, false)

            val builder = AlertDialog.Builder(context)

            builder.setView(dialogView)

            val alertDialog = builder.create()
            alertDialog.show()


            val titlealert: TextView = dialogView.findViewById(R.id.titleDialog)
            titlealert.text = alertTitle

            val msgAlertDialog: TextView = dialogView.findViewById(R.id.dialogMessageText)
            msgAlertDialog.text = alertMsg


            val cancle: TextView = dialogView.findViewById(R.id.cancle)
            cancle.setOnClickListener {
                alertDialog.dismiss()
            }

            val ok: TextView = dialogView.findViewById(R.id.ok)
            ok.setOnClickListener {
                onItemClick()
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


            val titlealert: TextView = dialogView.findViewById(R.id.titleDialog)
            titlealert.text = alertTitle

            val msgAlertDialog: TextView = dialogView.findViewById(R.id.dialogMessageText)
            msgAlertDialog.text = alertMsg

            val crrosbtn: ImageView = dialogView.findViewById(R.id.dialogDoneBtn)
            crrosbtn.setOnClickListener {
                alertDialog.dismiss()
            }


            val submitbtn: Button = dialogView.findViewById(R.id.okBtn)
            submitbtn.setOnClickListener {
                onItemClick()
                alertDialog.dismiss()
            }

        }



    }
}