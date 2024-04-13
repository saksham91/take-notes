package com.example.takenotes.ui

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takenotes.NoteStatus
import com.example.takenotes.Util
import com.example.takenotes.database.NoteDAO
import com.example.takenotes.database.NotesRepository
import com.example.takenotes.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(): ViewModel() {

    private lateinit var repository: NotesRepository

    private var _liveData = MutableLiveData<List<Note>>()
    val liveData: LiveData<List<Note>> = _liveData

    fun initialize(dao: NoteDAO) {
        repository = NotesRepository(dao)
        viewModelScope.launch(Dispatchers.Main) {
            repository.allNotes.collect{
                _liveData.value = it
            }
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

    fun getNote(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.getNote(id)
    }

    fun isContentUnchanged(status: NoteStatus, oldNote: Note, changedText: String): Boolean {
        return status == NoteStatus.EDITING_NOTE && changedText == oldNote.content
    }

    fun isTitleUnchanged(status: NoteStatus, oldNote: Note, changedText: String): Boolean {
        return status == NoteStatus.EDITING_NOTE && changedText == oldNote.title
    }

    fun updateBgImage(view: View): Int {
        return Util.getBgImageResource(view.id)
    }

    fun updateTextColor(view: View): Int {
        return Util.getTextColorResource(view.id)
    }

}