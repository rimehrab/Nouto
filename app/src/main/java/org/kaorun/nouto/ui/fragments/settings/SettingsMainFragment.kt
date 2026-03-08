package org.kaorun.nouto.ui.fragments.settings

import org.kaorun.nouto.R
import org.kaorun.nouto.ui.fragments.base.SettingsBaseFragment

class SettingsMainFragment : SettingsBaseFragment() {
    override val titleRes = R.string.settings
    override fun preferenceFragment() = SettingsMainPreferenceFragment()
}