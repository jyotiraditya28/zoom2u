package com.zoom2u_customer.ui.application.bid_request.active_bid_request

object ActiveItemProvider {
    val activebidItem: MutableList<ActiveBidItem> = ArrayList()

    private fun addActiveBid(offers: String) {

        val item = ActiveBidItem(offers)
        activebidItem.add(item)
    }

    init {
        addActiveBid("3")
        addActiveBid("8")
        addActiveBid("10")
        addActiveBid("2")

    }
}