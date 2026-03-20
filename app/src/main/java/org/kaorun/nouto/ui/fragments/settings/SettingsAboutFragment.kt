package org.kaorun.nouto.ui.fragments.settings

import org.kaorun.nouto.R
import org.kaorun.nouto.ui.fragments.base.SettingsBaseFragment
import org.kaorun.nouto.ui.fragments.settings.preferences.PreferenceAboutFragment

class SettingsAboutFragment : SettingsBaseFragment() {
    override val titleRes = R.string.about
    override fun preferenceFragment() = PreferenceAboutFragment()
}