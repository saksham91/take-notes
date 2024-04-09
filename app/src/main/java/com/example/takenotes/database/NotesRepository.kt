package com.example.takenotes.database

import com.example.takenotes.model.Note
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val noteDao: NoteDAO) {

    val notes: Flow<List<Note>> = noteDao.getAllNotes()

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