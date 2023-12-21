package com.example.takenotes.database

import androidx.lifecycle.LiveData
import com.example.takenotes.model.Note

class NotesRepository(private val noteDao: NoteDAO) {

    val notes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun delete(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun update(note: Note) {
        noteDao.updateNote(note)
    }

}