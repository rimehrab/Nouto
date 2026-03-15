package org.kaorun.nouto.ui.utils

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.HarmonizedColors
import com.google.android.material.color.HarmonizedColorsOptions
import org.kaorun.nouto.R

object ColorThemeHelper {
    const val KEY_DYNAMIC = "dynamic_colors"

    fun apply(activity: AppCompatActivity) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val options = HarmonizedColorsOptions.createMaterialDefaults()
        val isDynamicAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        val dynamic = prefs.getBoolean(KEY_DYNAMIC, isDynamicAvailable)
        if (dynamic) {
            activity.setTheme(R.style.Theme_Nouto)
            DynamicColors.applyToActivityIfAvailable(activity)
        } else {
           activity.setTheme(R.style.Theme_Nouto_Default)
        }
        HarmonizedColors.applyToContextIfAvailable(activity, options)
    }
}