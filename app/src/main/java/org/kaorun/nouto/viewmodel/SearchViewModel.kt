package org.kaorun.nouto.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.core.content.edit

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences(
        "search_prefs",
        Context.MODE_PRIVATE
    )

    private val _searchHistory = MutableLiveData(loadHistory())
    val searchHistory: LiveData<List<String>> = _searchHistory

    fun addToHistory(query: String) {
        if (query.isBlank()) return
        val history = loadHistory().toMutableList()
        history.remove(query)
        history.add(0, query)
        val trimmed = history.take(10)
        prefs.edit { putString("history", trimmed.joinToString(",")) }
        _searchHistory.value = trimmed
    }

    fun removeFromHistory(query: String) {
        val history = loadHistory().toMutableList()
        history.remove(query)
        prefs.edit { putString("history", history.joinToString(",")) }
        _searchHistory.value = history
    }

    private fun loadHistory(): List<String> {
        return prefs.getString("history", "")
            ?.split(",")
            ?.filter { it.isNotBlank() }
            ?: emptyList()
    }
}