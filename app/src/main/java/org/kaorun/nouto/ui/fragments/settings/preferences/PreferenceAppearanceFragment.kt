package org.kaorun.nouto.ui.fragments.settings.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.SwitchPreferenceCompat
import org.kaorun.nouto.R
import org.kaorun.nouto.ui.fragments.base.PreferenceBaseFragment
import org.kaorun.nouto.ui.utils.ColorThemeHelper
import org.kaorun.nouto.ui.utils.ThemeHelper

class PreferenceAppearanceFragment : PreferenceBaseFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_appearance, rootKey)

        findPreference<ListPreference>(ThemeHelper.KEY)
            ?.setOnPreferenceChangeListener { _, value ->
            ThemeHelper.apply(value)
            true
        }

        findPreference<SwitchPreferenceCompat>(ColorThemeHelper.KEY_DYNAMIC)
            ?.setOnPreferenceChangeListener { _, _ ->
                requireActivity().recreate()
                true
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imageView = LayoutInflater.from(requireContext())
            .inflate(R.layout.illustration_themes, listView, false)
        super.onViewCreated(view, savedInstanceState)
    }
}