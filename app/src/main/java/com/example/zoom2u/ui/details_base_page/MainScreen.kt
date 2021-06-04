package com.example.zoom2u.ui.details_base_page

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.zoom2u.R
import com.example.zoom2u.ui.details_base_page.history.HistoryFragment
import com.example.zoom2u.ui.details_base_page.home.home_fragment.HomeFragment

enum class MainScreen(@IdRes val menuItemId: Int,
                      @DrawableRes val menuItemIconId: Int,
                      @StringRes val titleStringId: Int,
                      val fragment: Fragment
) {
    LOGS(R.id.client_overview, R.drawable.ic_baseline_location_on_24, R.string.home, HomeFragment()),
    PROGRESS(R.id.client_contact, R.drawable.ic_site, R.string.home, HistoryFragment()),
    PROFILE(R.id.client_site, R.drawable.ic_site, R.string.home, HomeFragment()),
    WORK(R.id.work_history, R.drawable.ic_site, R.string.home, HomeFragment())
}

fun getMainScreenForMenuItem(menuItemId: Int): MainScreen? {
    for (mainScreen in MainScreen.values()) {
        if (mainScreen.menuItemId == menuItemId) {
            return mainScreen
        }
    }
    return null
}