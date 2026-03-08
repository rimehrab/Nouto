package org.kaorun.nouto.ui.components

import android.content.Context
import android.util.AttributeSet
import androidx.preference.ListPreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.kaorun.nouto.R

class MaterialListPreference(context: Context, attrs: AttributeSet?) : ListPreference(context, attrs) {
    override fun onClick() {
        MaterialAlertDialogBuilder(context)
            .setIcon(icon)
            .setTitle(title)
            .setSingleChoiceItems(entries, findIndexOfValue(value)) { dialog, which ->
                val newValue = entryValues[which].toString()
                if (callChangeListener(newValue)) {
                    setValue(newValue)
                }
                dialog.dismiss()
            }
            .setPositiveButton(R.string.cancel, null)
            .show()
    }
}