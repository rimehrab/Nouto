package org.kaorun.nouto.ui.fragments

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialFade
import com.onegravity.rteditor.RTManager
import com.onegravity.rteditor.api.RTApi
import com.onegravity.rteditor.api.RTMediaFactoryImpl
import com.onegravity.rteditor.api.RTProxyImpl
import com.onegravity.rteditor.api.format.RTFormat
import org.kaorun.nouto.R
import org.kaorun.nouto.data.Note
import org.kaorun.nouto.databinding.FragmentNoteBinding
import org.kaorun.nouto.ui.components.TextStyleFloatingToolbar
import org.kaorun.nouto.ui.fragments.base.BaseFragment
import org.kaorun.nouto.ui.utils.InsetsHandler
import org.kaorun.nouto.viewmodel.NotesViewModel

class NoteFragment : BaseFragment(R.layout.fragment_note) {
    private lateinit var rtManager: RTManager
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotesViewModel by navGraphViewModels(R.id.nav_graph)
    private val args: NoteFragmentArgs by navArgs()
    private var note: Note? = null
    private var isDeleting = false
    private var isSaved = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRtEditor(savedInstanceState)
        setupNote()
        setupInsets()
        setupToolbar()
        setupListeners()
        showKeyboard()
    }

    private fun setupRtEditor(savedInstanceState: Bundle?) {
        val rtApi = RTApi(
            requireContext(),
            RTProxyImpl(requireActivity()),
            RTMediaFactoryImpl(requireContext(), true)
        )

        rtManager = RTManager(rtApi, savedInstanceState)
        rtManager.registerEditor(binding.noteTitle, true)
        rtManager.registerEditor(binding.noteContent, true)
    }

    private fun setupNote() {
        val noteId = args.noteId

        if (noteId != -1) {
            viewModel.getNote(noteId).observe(viewLifecycleOwner) { existingNote ->
                existingNote?.let {
                    note = it
                    binding.noteTitle.setRichTextEditing(true, it.title)
                    binding.noteContent.setRichTextEditing(true, it.content)
                    binding.noteTitle.setSelection(binding.noteTitle.text?.length ?: 0)
                }
            }
        } else {
            binding.noteTitle.setRichTextEditing(true, null)
            binding.noteContent.setRichTextEditing(true, null)
        }
    }

    private fun setupToolbar() {
        val textStyleFloatingToolbar = TextStyleFloatingToolbar(
            rtManager,
            binding.styleBold,
            binding.styleItalic,
            binding.styleUnderline,
            listOf(binding.noteTitle, binding.noteContent)
        )
        textStyleFloatingToolbar.setupFloatingToolbar()
    }

    private fun setupListeners() {
        setupChangeListener()

        binding.buttonSave.setOnClickListener {
            closeNoteFragment()
        }

        binding.topAppBar.setNavigationOnClickListener {
            closeNoteFragment()
        }

        binding.topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete -> {
                    isDeleting = true
                    note?.let { viewModel.deleteNote(it) }
                    closeNoteFragment()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupChangeListener() {
        val watcher = object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                val titleChanged = binding.noteTitle.getText(RTFormat.HTML)
                    .orEmpty().toPlainText() != (note?.title?.toPlainText() ?: "")
                val contentChanged = binding.noteContent.getText(RTFormat.HTML)
                    .orEmpty().toPlainText() != (note?.content?.toPlainText() ?: "")
                setButtonVisible(titleChanged || contentChanged)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        binding.noteTitle.addTextChangedListener(watcher)
        binding.noteContent.addTextChangedListener(watcher)
    }

    private fun setButtonVisible(isChanged: Boolean) {
        val fade = MaterialFade().apply {
            duration = if (isChanged) 150L else 84L
        }
        TransitionManager.beginDelayedTransition(binding.root, fade)
        binding.buttonSave.isVisible = isChanged
    }

    private fun setupInsets() {
        InsetsHandler.applyViewInsets(
            binding.appBarLayout,
            isTopPaddingEnabled = true,
            isBottomPaddingEnabled = false
        )
        InsetsHandler.applyViewInsets(binding.contentContainer, false)
        InsetsHandler.applyImeInsets(binding.scrollView)

        (enterTransition as? androidx.transition.Transition)?.addListener(
            object : androidx.transition.Transition.TransitionListener {
                override fun onTransitionEnd(transition: androidx.transition.Transition) {
                    InsetsHandler.applyImeInsets(binding.floatingToolbar)
                }
                override fun onTransitionStart(transition: androidx.transition.Transition) {}
                override fun onTransitionCancel(transition: androidx.transition.Transition) {
                    InsetsHandler.applyImeInsets(binding.floatingToolbar)
                }
                override fun onTransitionPause(transition: androidx.transition.Transition) {}
                override fun onTransitionResume(transition: androidx.transition.Transition) {}
            }
        ) ?: InsetsHandler.applyImeInsets(binding.floatingToolbar)
    }

    private fun saveNote() {
        val title = binding.noteTitle.getText(RTFormat.HTML).orEmpty()
        val content = binding.noteContent.getText(RTFormat.HTML).orEmpty()
        val titleText = title.toPlainText()
        val contentText = content.toPlainText()

        if (note == null) {
            if (titleText.isNotEmpty() || contentText.isNotEmpty()) {
                viewModel.addNote(title, content, System.currentTimeMillis())
            }
            return
        }

        if (note!!.title?.toPlainText() != titleText || note!!.content?.toPlainText() != contentText) {
            viewModel.updateNote(note!!.copy(title = title, content = content, time = System.currentTimeMillis()))
        }
    }

    private fun showKeyboard() {
        binding.noteTitle.requestFocus()
        WindowCompat.getInsetsController(requireActivity().window, binding.noteTitle)
            .show(WindowInsetsCompat.Type.ime())
//        (enterTransition as? androidx.transition.Transition)?.addListener(
//            object : androidx.transition.Transition.TransitionListener {
//                override fun onTransitionEnd(transition: androidx.transition.Transition) {
//                    binding.noteTitle.requestFocus()
//                    WindowCompat.getInsetsController(requireActivity().window, binding.noteTitle)
//                        .show(WindowInsetsCompat.Type.ime())
//                }
//                override fun onTransitionStart(transition: androidx.transition.Transition) {}
//                override fun onTransitionCancel(transition: androidx.transition.Transition) {}
//                override fun onTransitionPause(transition: androidx.transition.Transition) {}
//                override fun onTransitionResume(transition: androidx.transition.Transition) {}
//            }
//        )
    }

    private fun closeNoteFragment() {
        TransitionManager.endTransitions(binding.root.parent as ViewGroup)
        findNavController().popBackStack()
    }

    private fun String.toPlainText() = Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).toString().trim()

    override fun onStop() {
        super.onStop()
        if (!isDeleting && !isSaved) {
            saveNote()
            isSaved = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        rtManager.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rtManager.onDestroy(requireActivity().isFinishing)
        _binding = null
    }
}