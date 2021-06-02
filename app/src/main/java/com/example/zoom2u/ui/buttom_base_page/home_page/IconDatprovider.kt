package com.example.zoom2u.ui.buttom_base_page.home_page

import com.example.zoom2u.R

object IconDatprovider {
    val iconList: MutableList<Icon> = ArrayList()

    private fun addIcon( image: Int,text: String) {

        val item = Icon(image,text)
        iconList.add(item)
    }

    init {
        addIcon(R.drawable.ic_baseline_folder_24,"Documents")
        addIcon(R.drawable.ic_baseline_computer_24,"Satchel,laptops")
        addIcon(R.drawable.ic_site,"Small box")
        addIcon(R.drawable.ic_site,"Cakes, flowers,delicates")
        addIcon(R.drawable.ic_baseline_folder_24,"Large box")
        addIcon(R.drawable.ic_site,"Large items")
    }
}