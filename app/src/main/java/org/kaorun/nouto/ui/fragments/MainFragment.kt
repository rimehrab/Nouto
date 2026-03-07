package org.kaorun.nouto.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.transition.TransitionManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.sidesheet.SideSheetDialog
import org.kaorun.nouto.R
import org.kaorun.nouto.databinding.FragmentMainBinding
import org.kaorun.nouto.ui.adapter.NoteAdapter
import org.kaorun.nouto.ui.components.MainSearchView
import org.kaorun.nouto.ui.model.LayoutMode
import org.kaorun.nouto.ui.utils.InsetsHandler
import org.kaorun.nouto.ui.utils.MarginItemDecoration
import org.kaorun.nouto.viewmodel.NotesViewModel
import org.kaorun.nouto.viewmodel.SearchViewModel

class MainFragment : BaseFragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteAdapter: NoteAdapter
    private val viewModel: NotesViewModel by navGraphViewModels(R.id.nav_graph)
    private val searchViewModel: SearchViewModel by viewModels()
    private var layoutMode = LayoutMode.LINEAR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = fadeAnimation(false)
        reenterTransition = fadeAnimation(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeNotes()
        setupRecyclerView()
        setupLayoutMode()
        setupSearchView()
        setupListeners()
        setupInsets()
    }

    private fun observeNotes() {
        viewModel.displayedNotes.observe(viewLifecycleOwner) { notes ->
            noteAdapter.submitList(notes) {
                binding.recyclerView.post {
                    binding.recyclerView.invalidateItemDecorations()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(
            onItemClick = {note -> openNoteFragment(note.id)},
            onDeleteClick = {note -> viewModel.deleteNote(note)}
        )
        binding.recyclerView.adapter = noteAdapter
    }

    private fun setupListeners() {
        setupNavigationButton()

        binding.fab.setOnClickListener {
            openNoteFragment(null)
        }

        binding.layoutButton.setOnClickListener {
            switchLayout()
        }
    }

    private fun setupSideSheet() {
        val sideSheetDialog = SideSheetDialog(requireContext())

        with(sideSheetDialog) {
            setContentView(R.layout.side_sheet)
            setFitsSystemWindows(false)
            show()
            setSheetEdge(Gravity.START)
        }

        val navigationView =
            sideSheetDialog.findViewById<NavigationView>(R.id.navigation_view)!!
        InsetsHandler.applyViewInsets(navigationView)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.settings -> {
                    sideSheetDialog.hide()
                    openSettingsFragment()
                }
                else -> sideSheetDialog.hide()
            }
            true
        }
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

    private fun setupNavigationButton() {
        viewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            if (query.isNullOrBlank()) {
                binding.navigationButton.setIconResource(R.drawable.menu_24px)
                binding.navigationButton.setOnClickListener { setupSideSheet() }
            } else {
                binding.navigationButton.setIconResource(R.drawable.arrow_back_24px)
                binding.navigationButton.setOnClickListener {
                    viewModel.setSearchQuery(null)
                    binding.searchBar.clearText()
                    binding.searchBar.textCentered = true
                }
            }
        }
    }

    private fun setupSearchView() {
        val searchResetCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                viewModel.setSearchQuery(null)
                binding.searchBar.clearText()
                binding.searchBar.textCentered = true
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            searchResetCallback
        )

        viewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            searchResetCallback.isEnabled = !query.isNullOrBlank()
        }
        MainSearchView(
            binding.searchView,
            binding.searchBar,
            binding.fab,
            binding.searchRecyclerView,
            viewModel,
            searchViewModel,
            resources,
            requireActivity(),
            viewLifecycleOwner
        )
    }

    private fun openNoteFragment(noteId: Int?) {
        TransitionManager.endTransitions(binding.root.parent as ViewGroup)
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToNoteFragment(noteId ?: -1)
        )
    }

    private fun openSettingsFragment() {
        TransitionManager.endTransitions(binding.root.parent as ViewGroup)
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToSettingsFragment()
        )
    }

    private fun setupLayoutMode() {
        viewModel.isGridMode.observe(viewLifecycleOwner) { isGrid ->
            layoutMode = if (isGrid) LayoutMode.GRID else LayoutMode.LINEAR
            applyLayoutMode()
        }
    }

    private fun applyLayoutMode() {
        binding.layoutButton.setIconResource(
            if (layoutMode == LayoutMode.GRID) R.drawable.view_agenda_24px
            else R.drawable.grid_view_24px
        )

        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(layoutMode.spanCount, RecyclerView.VERTICAL)

        repeat(binding.recyclerView.itemDecorationCount) {
            binding.recyclerView.removeItemDecorationAt(0)
        }

        binding.recyclerView.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.recycler_view_outer_margin
                ),
                resources.getDimensionPixelSize(
                    R.dimen.recycler_view_inner_margin
                ),
                layoutMode.spanCount
            )
        )

        binding.recyclerView.clipChildren = layoutMode == LayoutMode.GRID
    }

    private fun switchLayout() {
        val newMode = layoutMode != LayoutMode.GRID
        viewModel.setGridMode(newMode)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}