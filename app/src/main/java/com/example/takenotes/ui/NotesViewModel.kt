package com.example.takenotes.ui

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.takenotes.database.NoteDAO
import com.example.takenotes.database.NotesDatabase
import com.example.takenotes.database.NotesRepository
import com.example.takenotes.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class NotesViewModel(): ViewModel() {

    private lateinit var repository: NotesRepository

    fun initialize(dao: NoteDAO) {
        repository = NotesRepository(dao)
    }

    fun getAllNotes() = viewModelScope.launch(Dispatchers.IO) {
        repository.notes.collect{

        }
    }

    fun insertNote(content: String, title: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(Note(content = content,
            title = title))
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

}