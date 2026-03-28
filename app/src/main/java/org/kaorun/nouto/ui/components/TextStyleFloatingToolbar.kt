package org.kaorun.nouto.ui.components

import android.graphics.Typeface
import android.view.View
import android.view.View.AccessibilityDelegate
import android.view.accessibility.AccessibilityEvent
import com.google.android.material.button.MaterialButton
import com.onegravity.rteditor.RTEditText
import com.onegravity.rteditor.RTManager
import com.onegravity.rteditor.effects.Effect
import com.onegravity.rteditor.effects.Effects
import com.onegravity.rteditor.spans.BoldSpan
import com.onegravity.rteditor.spans.ItalicSpan
import com.onegravity.rteditor.spans.UnderlineSpan
import com.onegravity.rteditor.spans.RTSpan

class TextStyleFloatingToolbar(
    private val rtManager: RTManager,
    private val styleBold: MaterialButton,
    private val styleItalic: MaterialButton,
    private val styleUnderline: MaterialButton,
    private val fields: List<RTEditText>
) {
    fun setupFloatingToolbar() {
        setupStyleButton(styleBold, Effects.BOLD, Typeface.BOLD)
        setupStyleButton(styleItalic, Effects.ITALIC, Typeface.ITALIC)
        setupStyleButton(styleUnderline, Effects.UNDERLINE, null)
        fields.forEach { setupTextClassifier(it) }
    }

    private fun setupStyleButton(
        button: MaterialButton,
        effect: Effect<Boolean, out RTSpan<Boolean>>,
        typeface: Int?,
    ) {
        button.setOnClickListener {
            val isActive = when (typeface) {
                Typeface.BOLD -> styleBold.isChecked
                Typeface.ITALIC -> styleItalic.isChecked
                null -> styleUnderline.isChecked
                else -> false
            }
            button.isChecked = isActive
            rtManager.onEffectSelected(effect, isActive)
        }
    }

    private fun setupTextClassifier(textbox: RTEditText) {
        textbox.accessibilityDelegate = object : AccessibilityDelegate() {
            override fun sendAccessibilityEvent(host: View, eventType: Int) {
                super.sendAccessibilityEvent(host, eventType)
                if (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {
                    updateButtonsForSelection(textbox)
                }
            }
        }
    }

    private fun updateButtonsForSelection(textbox: RTEditText) {
        val spannable = textbox.text ?: return
        val start = textbox.selectionStart
        val end = textbox.selectionEnd

        styleBold.isChecked = spannable.getSpans(start, end, BoldSpan::class.java).isNotEmpty()
        styleItalic.isChecked = spannable.getSpans(start, end, ItalicSpan::class.java).isNotEmpty()
        styleUnderline.isChecked = spannable.getSpans(start, end, UnderlineSpan::class.java).isNotEmpty()
    }
}