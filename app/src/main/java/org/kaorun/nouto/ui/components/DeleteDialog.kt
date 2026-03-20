package org.kaorun.nouto.ui.components

import android.content.Context
import android.content.res.Resources
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.kaorun.nouto.R

object DeleteDialog {
    fun show(
        context: Context,
        resources: Resources,
        isMultipleNotes: Boolean = false,
        negativeAction: () -> Unit
    ) {
        val title = if (isMultipleNotes) resources.getString(R.string.delete_notes_dialog_title)
        else resources.getString(R.string.delete_note_dialog_title)
        val message = if (isMultipleNotes) resources.getString(R.string.delete_notes_dialog_message)
        else resources.getString(R.string.delete_note_dialog_message)
        MaterialAlertDialogBuilder(context)
            .setIcon(R.drawable.delete_forever_24px)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(resources.getString(R.string.delete)) { _, _ ->
                negativeAction()
            }
            .setPositiveButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}