package com.example.zoom2u.ui.details_base_page.profile.change_price

import com.example.zoom2u.ui.details_base_page.history.HistoryItem

object PriceChangeProvider {
    val priceChangeList: MutableList<PriceChange> = ArrayList()

    private fun addPriceChange(price_header:String) {

        val item = PriceChange(price_header)
        priceChangeList.add(item)
    }

    init {
        addPriceChange("Same Day - Pricing")
        addPriceChange("3 Hour - Pricing")
        addPriceChange("5 Hour - Pricing")
        addPriceChange("VIP - Pricing")

    }
}