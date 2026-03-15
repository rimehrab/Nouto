package org.kaorun.nouto.ui.components

import android.content.Context
import android.content.res.Resources
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.kaorun.nouto.R

object DeleteDialog {
    fun show(context: Context, resources: Resources, onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(context)
            .setIcon(R.drawable.delete_forever_24px)
            .setTitle(resources.getString(R.string.delete_note_dialog_title))
            .setMessage(resources.getString(R.string.delete_note_dialog_message))
            .setNegativeButton(resources.getString(R.string.delete)) { _, _ ->
                onConfirm()
            }
            .setPositiveButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}