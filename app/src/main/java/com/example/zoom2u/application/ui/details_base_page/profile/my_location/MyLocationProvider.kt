package com.example.zoom2u.application.ui.details_base_page.profile.my_location


object MyLocationProvider {

    val myLocationList: MutableList<MyLocation> = ArrayList()

    private fun addMyLocation(name:String,address:String) {

        val item = MyLocation(name,address)
        myLocationList.add(item)
    }

    init {
        addMyLocation("Mctavish","per street,Alton VIC 3018,Australia")
        addMyLocation("Zoom2u","per street,Alton VIC 3018,Australia")
        addMyLocation("David","per street,Alton VIC 3018,Australia")
        addMyLocation("Mat","per street,Alton VIC 3018,Australia")
        addMyLocation("Mctavish","per street,Alton VIC 3018,Australia")
        addMyLocation("Zoom2u","per street,Alton VIC 3018,Australia")
        addMyLocation("David","per street,Alton VIC 3018,Australia")
        addMyLocation("Mat","per street,Alton VIC 3018,Australia")


    }
}