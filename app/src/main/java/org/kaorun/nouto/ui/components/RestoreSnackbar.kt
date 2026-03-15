package org.kaorun.nouto.ui.components

import android.view.View
import com.google.android.material.snackbar.Snackbar
import org.kaorun.nouto.R
import org.kaorun.nouto.data.Note
import org.kaorun.nouto.viewmodel.NotesViewModel

object RestoreSnackbar {
    fun show(view: View, anchorView: View?, viewModel: NotesViewModel, note: Note) {
        Snackbar.make(view, R.string.note_restored_message, Snackbar.LENGTH_SHORT)
            .setAnchorView(anchorView)
            .setAction(R.string.undo) {
                viewModel.markDeleted(note)
            }
            .show()
    }
}