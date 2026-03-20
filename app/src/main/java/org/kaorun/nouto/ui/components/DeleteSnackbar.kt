package org.kaorun.nouto.ui.components

import android.view.View
import com.google.android.material.snackbar.Snackbar
import org.kaorun.nouto.R

object DeleteSnackbar {
    fun show(view: View, anchorView: View?, undoAction: () -> Unit) {
        Snackbar.make(view, R.string.note_deleted_message, Snackbar.LENGTH_SHORT)
            .setAnchorView(anchorView)
            .setAction(R.string.undo) {
                undoAction()
            }
            .show()
    }
}