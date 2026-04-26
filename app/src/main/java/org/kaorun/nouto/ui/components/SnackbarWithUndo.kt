package org.kaorun.nouto.ui.components

import android.view.View
import com.google.android.material.snackbar.Snackbar
import org.kaorun.nouto.R

object SnackbarWithUndo {
    fun show(
        view: View,
        anchorView: View?,
        message: String,
        undoAction: () -> Unit
    ) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setAnchorView(anchorView)
            .setAction(R.string.undo) {
                undoAction()
            }
            .show()
    }
}