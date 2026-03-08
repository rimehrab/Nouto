package org.kaorun.nouto.ui.fragments.settings

import android.os.Bundle
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.transition.TransitionManager
import org.kaorun.nouto.R
import org.kaorun.nouto.ui.fragments.base.PreferenceBaseFragment

class SettingsMainPreferenceFragment : PreferenceBaseFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_main, rootKey)

        findPreference<Preference>("screen_appearance")?.setOnPreferenceClickListener {
            val parentView = requireParentFragment().requireView().parent as ViewGroup
            TransitionManager.endTransitions(parentView)
            requireParentFragment().findNavController()
                .navigate(R.id.action_settingsMainFragment_to_settingsAppearanceFragment)
            true
        }
    }
}