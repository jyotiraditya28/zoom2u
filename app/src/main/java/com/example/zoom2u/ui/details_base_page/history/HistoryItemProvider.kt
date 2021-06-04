package com.example.zoom2u.ui.details_base_page.history

object HistoryItemProvider {
    val historyItem: MutableList<HistoryItem> = ArrayList()

    private fun addPrice(name:String) {

        val item = HistoryItem(name)
        historyItem.add(item)
    }

    init {
        addPrice("Johnny P.")
        addPrice("David F.")
        addPrice("Fullbert A.")
        addPrice("Johnny P.")

    }
}