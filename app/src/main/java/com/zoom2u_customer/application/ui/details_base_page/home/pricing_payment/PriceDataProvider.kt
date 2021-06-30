package com.zoom2u_customer.application.ui.details_base_page.home.pricing_payment

object PriceDataProvider {

    val priceList: MutableList<Price> = ArrayList()

    private fun addPrice(price_tag:String,price_time : String) {

        val item = Price(price_tag,price_time)
        priceList.add(item)
    }

    init {
        addPrice("Same day","5.00pm today")
        addPrice("Same day","5.00pm today")
        addPrice("Same day","5.00pm today")
        addPrice("Same day","5.00pm today")

    }
}