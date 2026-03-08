package org.kaorun.nouto.ui.fragments.settings

import org.kaorun.nouto.R
import org.kaorun.nouto.ui.fragments.base.SettingsBaseFragment
import org.kaorun.nouto.ui.fragments.settings.preferences.PreferenceMainFragment

class SettingsMainFragment : SettingsBaseFragment() {
    override val titleRes = R.string.settings
    override fun preferenceFragment() = PreferenceMainFragment()
}