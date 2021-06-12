package com.example.zoom2u.ui.details_base_page

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.zoom2u.R
import com.example.zoom2u.ui.details_base_page.bid_quote_request.BidquoteRequestFragment
import com.example.zoom2u.ui.details_base_page.history.HistoryFragment
import com.example.zoom2u.ui.details_base_page.home.home_fragment.HomeFragment
import com.example.zoom2u.ui.details_base_page.profile.ProfileFragment

enum class MainScreen(@IdRes val menuItemId: Int,
                      @DrawableRes val menuItemIconId: Int,
                      @StringRes val titleStringId: Int,
                      val fragment: Fragment
) {
    LOGS(R.id.client_overview, R.drawable.ic_home_mono_icon, R.string.home, HomeFragment()),
    PROGRESS(R.id.client_contact, R.drawable.ic_bookings_mono_icon, R.string.home, HistoryFragment()),
    PROFILE(R.id.client_site, R.drawable.ic_history_mono_icon, R.string.home, BidquoteRequestFragment()),
    WORK(R.id.work_history, R.drawable.ic_profile_mono_icon, R.string.home, ProfileFragment())
}

fun getMainScreenForMenuItem(menuItemId: Int): MainScreen? {
    for (mainScreen in MainScreen.values()) {
        if (mainScreen.menuItemId == menuItemId) {
            return mainScreen
        }
    }
    return null
}