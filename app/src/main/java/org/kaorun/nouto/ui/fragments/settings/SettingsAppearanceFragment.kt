package org.kaorun.nouto.ui.fragments.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import org.kaorun.nouto.R
import org.kaorun.nouto.ui.fragments.base.SettingsBaseFragment

class SettingsAppearanceFragment : SettingsBaseFragment() {
    override val titleRes = R.string.appearance
    override fun preferenceFragment() = SettingsAppearancePreferenceFragment()
}