package com.zoom2u_customer.ui.buttom_navigation_package.chat

import com.zoom2u_customer.R


object ChatDataProvider {

    val chatItem: MutableList<Chat> = ArrayList()

    private fun addChatItem(
        dp: Int, name: String, time: String,
        lastmessage: String, messcount: String
    ) {

        val item = Chat(dp, name, time, lastmessage, messcount)
        chatItem.add(item)
    }

    init {
        addChatItem(
            R.drawable.profile, "Fulbert A.", "20 min ago", "Thank you for your consideration",
            "3"
        )

        addChatItem(
            R.drawable.profile, "Fulbert A.", "20 min ago", "Thank you for your consideration",
            "3"
        )

        addChatItem(
            R.drawable.profile, "Fulbert A.", "20 min ago", "Thank you for your consideration",
            "3"
        )
        addChatItem(
            R.drawable.profile, "Fulbert A.", "20 min ago", "Thank you for your consideration",
            "3"
        )

        addChatItem(
            R.drawable.profile, "Fulbert A.", "20 min ago", "Thank you for your consideration",
            "3"
        )

        addChatItem(
            R.drawable.profile, "Fulbert A.", "20 min ago", "Thank you for your consideration",
            "3"
        )
        addChatItem(
            R.drawable.profile, "Fulbert A.", "20 min ago", "Thank you for your consideration",
            "3"
        )
        addChatItem(
            R.drawable.profile, "Fulbert A.", "20 min ago", "Thank you for your consideration",
            "3"
        )

        addChatItem(
            R.drawable.profile, "Fulbert A.", "20 min ago", "Thank you for your consideration",
            "3"
        )
        addChatItem(
            R.drawable.profile, "Fulbert A.", "20 min ago", "Thank you for your consideration",
            "3"
        )


    }
}