package com.zoom2u_customer.utility

class Zoom2uContractProvider {
    companion object {

        val DB_NAME = "locate2u"
        var galleryPermissions = arrayOf(
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
        )

        var cameraPermissions = arrayOf(
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE" //"android.permission.CAMERA",
        )
    }
}