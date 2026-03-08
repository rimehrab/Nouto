package org.kaorun.nouto.ui.fragments.settings

import org.kaorun.nouto.R
import org.kaorun.nouto.ui.fragments.base.SettingsBaseFragment
import org.kaorun.nouto.ui.fragments.settings.preferences.PreferenceAppearanceFragment

class SettingsAppearanceFragment : SettingsBaseFragment() {
    override val titleRes = R.string.appearance
    override fun preferenceFragment() = PreferenceAppearanceFragment()
}