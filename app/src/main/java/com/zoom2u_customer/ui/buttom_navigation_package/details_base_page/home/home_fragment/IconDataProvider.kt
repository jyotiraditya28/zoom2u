package com.zoom2u_customer.ui.buttom_navigation_package.details_base_page.home.home_fragment

import com.zoom2u_customer.R


object IconDataProvider {
    val iconList: MutableList<Icon> = ArrayList()

    private fun addIcon( image: Int,text: String) {

        val item = Icon(image,text,0)
        iconList.add(item)
    }

    init {
        addIcon(R.drawable.ic_documents,"Documents")
        addIcon(R.drawable.ic_satchel_and_laptops,"Satchel,laptops")
        addIcon(R.drawable.ic_small_box,"Small box")
        addIcon(R.drawable.ic_cakes_flowers_delicates,"Cakes, flowers,delicates")
        addIcon(R.drawable.ic_large_box,"Large box")
        addIcon(R.drawable.ic_large_items,"Large items")

    }
}