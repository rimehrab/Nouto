package org.kaorun.nouto.ui.components

import android.graphics.Typeface
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.textclassifier.SelectionEvent
import android.view.textclassifier.TextClassifier
import com.google.android.material.button.MaterialButton
import com.onegravity.rteditor.RTEditText
import com.onegravity.rteditor.RTManager
import com.onegravity.rteditor.effects.Effect
import com.onegravity.rteditor.effects.Effects
import com.onegravity.rteditor.spans.RTSpan

class TextStyleFloatingToolbar(
    private val rtManager: RTManager,
    private val styleBold: MaterialButton,
    private val styleItalic: MaterialButton,
    private val styleUnderline: MaterialButton,
    private val fields: List<RTEditText>
) {
    private var isBold = false
    private var isItalic = false
    private var isUnderline = false

    fun setupFloatingToolbar() {
        setupStyleButton(styleBold, Effects.BOLD, Typeface.BOLD) {
            isBold = it
        }
        setupStyleButton(styleItalic, Effects.ITALIC, Typeface.ITALIC) {
            isItalic = it
        }
        setupStyleButton(styleUnderline, Effects.UNDERLINE, null) {
            isUnderline = it
        }
        fields.forEach { setupTextClassifier(it) }
    }

    private fun setupStyleButton(
        button: MaterialButton,
        effect: Effect<Boolean, out RTSpan<Boolean>>,
        typeface: Int?,
        flagUpdater: (Boolean) -> Unit
    ) {
        button.setOnClickListener {
            val active = when (typeface) {
                Typeface.BOLD -> !isBold
                Typeface.ITALIC -> !isItalic
                null -> !isUnderline
                else -> false
            }
            flagUpdater(active)
            button.isChecked = active
            rtManager.onEffectSelected(effect, active)
        }
    }

    private fun setupTextClassifier(textbox: RTEditText) {
        textbox.setTextClassifier(
            object : TextClassifier {
            override fun onSelectionEvent(event: SelectionEvent) = updateButtonsForSelection(textbox)
        })
        textbox.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) updateButtonsForSelection(textbox)
        }
    }

    private fun updateButtonsForSelection(textbox: RTEditText) {
        val spannable = textbox.text ?: return
        val start = textbox.selectionStart
        val end = textbox.selectionEnd

        isBold = spannable.getSpans(start, end, StyleSpan::class.java).any {
            it.style == Typeface.BOLD
        }
        isItalic = spannable.getSpans(start, end, StyleSpan::class.java).any {
            it.style == Typeface.ITALIC
        }
        isUnderline = spannable.getSpans(start, end, UnderlineSpan::class.java).any {
            spannable.getSpanStart(it) <= end && spannable.getSpanEnd(it) >= start
        }

        styleBold.isChecked = isBold
        styleItalic.isChecked = isItalic
        styleUnderline.isChecked = isUnderline
    }
}