package com.example.zoom2u.ui.details_base_page.home.home_fragment

import com.example.zoom2u.R

object IconDataProvider {
    val iconList: MutableList<Icon> = ArrayList()

    private fun addIcon( image: Int,text: String) {

        val item = Icon(image,text)
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