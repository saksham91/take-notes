package com.example.takenotes.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.takenotes.NoteStatus
import com.example.takenotes.R
import com.example.takenotes.database.NotesDatabase
import com.example.takenotes.databinding.FragmentNotesDetailBinding
import com.example.takenotes.model.Note

class NotesDetailFragment : Fragment() {

    private var _viewBinding: FragmentNotesDetailBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val notesViewModel: NotesViewModel by viewModels()
    private val args: NotesDetailFragmentArgs by navArgs()
    private var existingNote: Note? = null
    private var noteStatus = NoteStatus.NEW_NOTE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentNotesDetailBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = NotesDatabase.getNotesDatabase(activity as Activity).notesDao()
        notesViewModel.initialize(dao)
        if (args.noteId != 0L) {
            noteStatus = NoteStatus.EDITING_NOTE
            viewBinding.saveIcon.visibility = View.GONE
            observe()
        }
        initUI()
    }

    private fun initUI() {
        viewBinding.backNavigation.setOnClickListener {
            findNavController().navigateUp()
        }
        viewBinding.saveIcon.setOnClickListener {
            val title = viewBinding.noteTitle.text
            if (title?.toString().isNullOrEmpty()) {
                Toast.makeText(
                    this.context,
                    "Please select a title for the note",
                    Toast.LENGTH_SHORT).show()
            } else {
                saveNote()
            }
        }
        viewBinding.deleteIcon.visibility = if (noteStatus == NoteStatus.NEW_NOTE) View.GONE else View.VISIBLE
        viewBinding.deleteIcon.setOnClickListener {
            deleteNote()
        }
    }

    private fun observe() {
        notesViewModel.liveData.observe(viewLifecycleOwner) { it ->
            existingNote = it.find { it.id == args.noteId }
            if (existingNote != null) populateContent(existingNote!!)
        }
    }

    private fun populateContent(note: Note) {
        viewBinding.noteTitle.setText(note.title)
        viewBinding.noteContents.setText(note.content)
    }

    override fun onResume() {
        super.onResume()
        if (noteStatus == NoteStatus.EDITING_NOTE) {
            textChangeListeners()
        }
    }

    private fun saveNote() {
        val note = viewBinding.noteContents.text.toString()
        val title = viewBinding.noteTitle.text.toString()
        if (noteStatus == NoteStatus.NEW_NOTE) {
            notesViewModel.insertNote(note, title)
            Toast.makeText(this.context, "Note saved", Toast.LENGTH_SHORT).show()
        } else {
            val updatedNote = Note(id = existingNote!!.id,
                title = title,
                content = note,
                dateCreated = existingNote!!.dateCreated)
            notesViewModel.updateNote(updatedNote)
            Toast.makeText(this.context, "Note updated", Toast.LENGTH_SHORT).show()
        }
        findNavController().navigateUp()
    }

    private fun deleteNote() {
        if (existingNote != null) {
            Toast.makeText(this.context, "Note with title ${existingNote?.title} deleted", Toast.LENGTH_SHORT).show()
            notesViewModel.deleteNote(existingNote!!)
            findNavController().navigateUp()
        }
    }

    private fun textChangeListeners() {
        viewBinding.noteContents.doAfterTextChanged {
            if (notesViewModel.isContentUnchanged(noteStatus, existingNote!!, it.toString())) {
                viewBinding.saveIcon.visibility = View.GONE
            } else {
                viewBinding.saveIcon.visibility = View.VISIBLE
            }
        }
        viewBinding.noteTitle.doAfterTextChanged {
            if (notesViewModel.isTitleUnchanged(noteStatus, existingNote!!, it.toString())) {
                viewBinding.saveIcon.visibility = View.GONE
            } else {
                viewBinding.saveIcon.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotesDetailFragment().apply {}
    }
}