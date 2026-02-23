package org.kaorun.nouto.ui.components

import android.content.res.Resources
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import org.kaorun.nouto.R
import org.kaorun.nouto.ui.adapter.SearchAdapter
import org.kaorun.nouto.ui.utils.MarginItemDecoration
import org.kaorun.nouto.viewmodel.NotesViewModel
import org.kaorun.nouto.viewmodel.SearchViewModel

class MainSearchView(
    private val searchView: SearchView,
    private val searchBar: SearchBar,
    private val fab: FloatingActionButton,
    private val searchRecyclerView: RecyclerView,
    private val viewModel: NotesViewModel,
    private val searchViewModel: SearchViewModel,
    private val resources: Resources,
    private val activity: FragmentActivity,
    lifecycleOwner: LifecycleOwner
) {
    private lateinit var searchAdapter: SearchAdapter
    private val backCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            searchView.hide()
        }
    }

    init {
        searchView.setupWithSearchBar(searchBar)
        activity.onBackPressedDispatcher.addCallback(lifecycleOwner, backCallback)
        setupRecyclerView()
        setupSearchView()
        searchViewModel.searchHistory.observe(lifecycleOwner) { history ->
            searchAdapter.submitList(history)
        }
    }

    private fun setupSearchView() {
        searchView.addTransitionListener { _, _, newState ->
            when (newState) {
                SearchView.TransitionState.SHOWING -> {
                    backCallback.isEnabled = true
                    fab.hide()
                }
                SearchView.TransitionState.HIDDEN -> {
                    backCallback.isEnabled = false
                    fab.show()
                }
                else -> Unit
            }
        }

        searchView.editText.setOnEditorActionListener { textView, _, _ ->
            val query = textView.text.toString().trim()
            viewModel.setSearchQuery(query.ifBlank { null })
            searchViewModel.addToHistory(query)
            searchBar.setText(query)
            searchBar.textCentered = query.isBlank()
            searchView.hide()
            true
        }
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter(
            onItemClick = { query ->
                viewModel.setSearchQuery(query)
                searchBar.setText(query)
                searchView.hide()
            },
            onDeleteClick = { query ->
                searchViewModel.removeFromHistory(query)
            }
        )
        searchRecyclerView.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        searchRecyclerView.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.recycler_view_outer_margin
                ),
                resources.getDimensionPixelSize(
                    R.dimen.recycler_view_segmented_list_margin
                ),
                1
            )
        )
    }
}