package org.kaorun.nouto.ui.fragments

import android.os.Bundle
import org.kaorun.nouto.R


class SettingsMainFragment : SettingsBaseFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_main, rootKey)
    }
}