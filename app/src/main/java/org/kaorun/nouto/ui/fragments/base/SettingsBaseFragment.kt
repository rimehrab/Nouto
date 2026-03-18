package org.kaorun.nouto.ui.fragments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import androidx.transition.TransitionManager
import org.kaorun.nouto.R
import org.kaorun.nouto.databinding.FragmentSettingsBinding
import org.kaorun.nouto.ui.utils.InsetsHandler

abstract class SettingsBaseFragment : BaseFragment(R.layout.fragment_settings) {
    private var _binding: FragmentSettingsBinding? = null
    protected val binding get() = _binding!!

    abstract val titleRes: Int
    private var isExpanded = false
    abstract fun preferenceFragment(): PreferenceFragmentCompat

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private const val KEY_IS_EXPANDED = "appbar_is_expanded"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            isExpanded = savedInstanceState.getBoolean(KEY_IS_EXPANDED, true)
        }


        InsetsHandler.applyViewInsets(
            binding.appBarLayout,
            isTopPaddingEnabled = true,
            isBottomPaddingEnabled = false
        )

        binding.toolbar.title = getString(titleRes)
        binding.appBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
            isExpanded = verticalOffset == 0
        }
        binding.appBarLayout.setExpanded(isExpanded)
        binding.toolbar.setNavigationOnClickListener {
            TransitionManager.endTransitions(binding.root.parent as ViewGroup)
            findNavController().popBackStack()
        }

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.preference_container, preferenceFragment())
                .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_EXPANDED, isExpanded)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}