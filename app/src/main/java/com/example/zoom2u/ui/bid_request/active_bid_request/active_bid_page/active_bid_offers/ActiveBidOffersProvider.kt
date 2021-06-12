package com.example.zoom2u.ui.bid_request.active_bid_request.active_bid_page.active_bid_offers

import com.example.zoom2u.ui.bid_request.active_bid_request.ActiveBidItem

object ActiveBidOffersProvider {
    val activebidOffers: MutableList<ActiveBidOffers> = ArrayList()



    init {
        addActiveBidOffer("$25")
        addActiveBidOffer("$45")
        addActiveBidOffer("$48")
        addActiveBidOffer("$50")
        addActiveBidOffer("$25")
        addActiveBidOffer("$45")
        addActiveBidOffer("$48")
        addActiveBidOffer("$50")

    }

    private fun addActiveBidOffer(price: String) {
        val item = ActiveBidOffers(price)
        activebidOffers.add(item)
    }
}