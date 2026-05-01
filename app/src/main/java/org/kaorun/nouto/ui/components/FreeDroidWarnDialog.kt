package org.kaorun.nouto.ui.components

import android.content.Context
import android.content.Intent
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.kaorun.nouto.R
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.appcompat.app.AlertDialog

object FreeDroidWarnDialog {
    fun show(context: Context, buildVersion: Int): AlertDialog? {
        val prefManager = PreferenceManager.getDefaultSharedPreferences(context)
        val versionCode = prefManager.getInt("versionCodeWarn", 0)

        val htmlMessage = "${context.resources.getString(R.string.dialog_keepandroidopen_warning)} <br> " +
                "<a href=\"${context.resources.getString(R.string.dialog_keepandroidopen_uri)}\">" +
                "${context.resources.getString(R.string.dialog_keepandroidopen_more_info)}</a>"

        if (buildVersion > versionCode) {
            val dialog = MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialAlertDialog_KeepAndroidOpen)
                .setIcon(R.drawable.warning)
                .setTitle(R.string.dialog_keepandroidopen_title)
                .setMessage(HtmlCompat.fromHtml(htmlMessage, HtmlCompat.FROM_HTML_MODE_COMPACT))
                .setPositiveButton(R.string.okay) { d, _ ->
                    prefManager.edit { putInt("versionCodeWarn", buildVersion) }
                    d.dismiss()
                }
                .setNegativeButton(R.string.dialog_keepandroidopen_solution) { _, _ ->
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            context.resources.getString(R.string.dialog_keepandroidopen_solution_uri).toUri()
                        )
                    )
                }
                .create()

            dialog.show()

            val messageTextView = dialog.findViewById<TextView>(android.R.id.message)
            messageTextView?.movementMethod = LinkMovementMethod.getInstance()

            return dialog
        }
        return null
    }
}