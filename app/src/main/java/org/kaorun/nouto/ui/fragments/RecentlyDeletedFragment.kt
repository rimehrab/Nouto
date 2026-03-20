package org.kaorun.nouto.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.transition.TransitionManager
import com.google.android.material.listitem.ListItemLayout
import com.google.android.material.listitem.SwipeableListItem
import org.kaorun.nouto.R
import org.kaorun.nouto.data.Note
import org.kaorun.nouto.databinding.FragmentRecentlyDeletedBinding
import org.kaorun.nouto.ui.adapter.NoteAdapter
import org.kaorun.nouto.ui.components.DeleteDialog
import org.kaorun.nouto.ui.components.RestoreSnackbar
import org.kaorun.nouto.ui.fragments.base.BaseFragment
import org.kaorun.nouto.ui.model.LayoutMode
import org.kaorun.nouto.ui.utils.InsetsHandler
import org.kaorun.nouto.ui.utils.MarginItemDecoration
import org.kaorun.nouto.viewmodel.NotesViewModel

class RecentlyDeletedFragment : BaseFragment(R.layout.fragment_recently_deleted) {
    private var _binding: FragmentRecentlyDeletedBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteAdapter: NoteAdapter
    private val viewModel: NotesViewModel by navGraphViewModels(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecentlyDeletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeNotes()
        setupRecyclerView()
        setupInsets()
        setupListeners()
    }

    private fun observeNotes() {
        viewModel.deletedNotes.observe(viewLifecycleOwner) { notes ->
            noteAdapter.submitList(notes) {
                binding.recyclerView.invalidateItemDecorations()
            }
            binding.notesEmptyLayout.root.isVisible = notes.isEmpty()
            binding.fab.isVisible = notes.isNotEmpty()
        }
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(
            onItemClick = {note -> openNoteFragment(note.id) },
            onDeleteClick = { note -> setupDeleteDialog(note) },
            onRestoreClick = { note ->
                val anchorView = if (noteAdapter.currentList.size == 1) null else binding.fab
                RestoreSnackbar.show(binding.root, anchorView) {
                    viewModel.markDeleted(note)
                }
                viewModel.unmarkDeleted(note)
            }
        )
        binding.recyclerView.adapter = noteAdapter
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(LayoutMode.LINEAR.spanCount, RecyclerView.VERTICAL)
        binding.recyclerView.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.recycler_view_outer_margin
                ),
                resources.getDimensionPixelSize(
                    R.dimen.recycler_view_inner_margin
                ),
                LayoutMode.LINEAR.spanCount
            )
        )
    }

    private fun setupInsets() {
        val fabMargin = resources.getDimensionPixelSize(R.dimen.fab_margin)
        InsetsHandler.applyViewInsets(
            binding.appBarLayout,
            isTopPaddingEnabled = true,
            isBottomPaddingEnabled = false
        )
        InsetsHandler.applyViewInsets(binding.recyclerView, false)
        InsetsHandler.applyViewInsets(binding.fab, fabMargin)
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener { closeRecentlyDeletedFragment() }
        binding.fab.setOnClickListener { setupDeleteDialog(noteAdapter.currentList) }
    }

    private fun setupDeleteDialog(note: Note) {
        val position = noteAdapter.currentList.indexOf(note)
        val holder = binding.recyclerView
            .findViewHolderForAdapterPosition(position) as NoteAdapter.NoteViewHolder
        (holder.itemView as ListItemLayout)
            .setSwipeState(SwipeableListItem.STATE_CLOSED, Gravity.END)

        DeleteDialog.show(requireContext(), resources) {
            viewModel.deleteNote(note)
        }
    }

    private fun setupDeleteDialog(notes: List<Note>) {
        DeleteDialog.show(requireContext(), resources, true) {
            binding.recyclerView.isVisible = false
            viewModel.deleteNotes(notes)
        }
    }

//    private fun setupRestoreDialog(note: Note) {
//        MaterialAlertDialogBuilder(requireContext())
//            .setIcon(R.drawable.restore_from_trash_24px)
//            .setTitle(resources.getString(R.string.restore_note_dialog_title))
//            .setMessage(resources.getString(R.string.restore_note_dialog_message))
//            .setPositiveButton(resources.getString(R.string.restore)) { _, _ ->
//                setupRestoreSnackbar(note)
//            }
//            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, _ ->
//                dialog.cancel()
//            }
//            .show()
//    }

    private fun openNoteFragment(noteId: Int) {
        TransitionManager.endTransitions(binding.root.parent as ViewGroup)
        findNavController().navigate(
            RecentlyDeletedFragmentDirections.actionRecentlyDeletedFragmentToNoteFragment(noteId)
        )
        binding.fab.hide()
    }

    private fun closeRecentlyDeletedFragment() {
        TransitionManager.endTransitions(binding.root.parent as ViewGroup)
        findNavController().popBackStack()
        binding.fab.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}