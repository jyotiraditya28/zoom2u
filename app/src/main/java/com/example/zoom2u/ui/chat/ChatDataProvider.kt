package com.example.zoom2u.ui.chat

import com.example.zoom2u.R


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