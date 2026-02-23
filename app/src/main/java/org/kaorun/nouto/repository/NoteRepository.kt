package org.kaorun.nouto.repository

import androidx.lifecycle.LiveData
import org.kaorun.nouto.data.Note
import org.kaorun.nouto.data.NoteDao

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    fun getNoteById(id: Int): LiveData<Note> = noteDao.getNoteById(id)

    fun searchNotes(query: String): LiveData<List<Note>> = noteDao.searchNotes(query)

    suspend fun insert(note: Note) = noteDao.insert(note)

    suspend fun update(note: Note) = noteDao.update(note)

    suspend fun delete(note: Note) = noteDao.delete(note)
}