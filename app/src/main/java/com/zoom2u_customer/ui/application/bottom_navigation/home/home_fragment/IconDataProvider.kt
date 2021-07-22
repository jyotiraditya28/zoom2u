package com.zoom2u_customer.ui.application.bottom_navigation.home.home_fragment

import com.zoom2u_customer.R


object IconDataProvider {
    val iconList: MutableList<Icon> = ArrayList()

    private fun addIcon( image: Int,text: String,desc : String,weight: Double,length : Int,
                         width : Int,height : Int,Category:String,Value:Int,ItemWeightKg:Int) {

        val item = Icon(image,text,desc,0,weight,length,width,height,Category,Value,ItemWeightKg)
        iconList.add(item)

    }

    init {
        addIcon(R.drawable.ic_documents,"Documents","A4 Envelopes, letters, or legal documents",0.5,40,20,10,"Documents",10,0)
        addIcon(R.drawable.ic_satchel_and_laptops,"Satchel,laptops","Laptop bags, satchels, or briefcases",3.toDouble(),50,30,20,"Bag",11,0)
        addIcon(R.drawable.ic_small_box,"Small box","Computer, microwaves, clothing, small kitchen appliances, case of beer",10.toDouble(),40,20,30,"Box",12,0)
        addIcon(R.drawable.ic_cakes_flowers_delicates,"Cakes, flowers,delicates","Single, double tier cakes, bouquets, pastries, pottery",4.toDouble(),20,20,60,"Flowers",13,0)
        addIcon(R.drawable.ic_large_box,"Large box","aintings, chairs, TVs, flat-pack furniture",25.toDouble(),20,20,60,"Large",14,0)
        addIcon(R.drawable.ic_large_items,"Large items","Engines, Cars, Large shipping containers, Whole pallets, Plants, Large boxes",0.0,0,0,0,"",0,0)

    }
}