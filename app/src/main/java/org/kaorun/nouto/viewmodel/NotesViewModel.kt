package org.kaorun.nouto.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import org.kaorun.nouto.data.Note
import org.kaorun.nouto.repository.NoteRepository
import kotlinx.coroutines.launch
import org.kaorun.nouto.data.NoteDatabase

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    private val _searchQuery = MutableLiveData<String?>(null)
    private val _isGridMode = MutableLiveData(false)
    private val allNotes: LiveData<List<Note>>
    val displayedNotes: LiveData<List<Note>>
    val searchQuery: LiveData<String?> = _searchQuery
    val isGridMode: LiveData<Boolean> = _isGridMode

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        allNotes = repository.allNotes
        displayedNotes = searchQuery.switchMap { query ->
            if (query.isNullOrBlank()) allNotes
            else repository.searchNotes(query)
        }
    }

    fun setSearchQuery(query: String?) {
        _searchQuery.value = query
    }

    fun setGridMode(isGrid: Boolean) {
        _isGridMode.value = isGrid
    }

    fun getNote(id: Int): LiveData<Note> = repository.getNoteById(id)

    fun addNote(
        title: String?,
        content: String?,
        time: Long = System.currentTimeMillis()
    ) {
        if (title.isNullOrBlank() && content.isNullOrBlank()) return
        viewModelScope.launch {
            repository.insert(
                Note(
                    title = title,
                    content = content,
                    time = time
                )
            )
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            if (note.title.isNullOrBlank() && note.content.isNullOrBlank()) {
                repository.delete(note)
            } else {
                repository.update(note)
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch { repository.delete(note) }
    }
}