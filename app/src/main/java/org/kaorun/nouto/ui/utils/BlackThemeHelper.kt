package org.kaorun.nouto.ui.utils

import android.content.Context
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import org.kaorun.nouto.R

object BlackThemeHelper {
    const val KEY = "black_theme"

    fun apply(context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val isBlackTheme = sharedPreferences.getBoolean(KEY, false)
        val themePref = sharedPreferences.getString(ThemeHelper.KEY, "system")

        val isDarkMode = when (themePref) {
            "dark" -> true
            "light" -> false
            else -> {
                val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                currentNightMode == Configuration.UI_MODE_NIGHT_YES
            }
        }

        if (isBlackTheme && isDarkMode) {
            context.setTheme(R.style.ThemeOverlay_Nouto_PureBlack)
        }
    }
}