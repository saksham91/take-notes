package com.example.takenotes.ui

import android.app.Activity
import android.os.Bundle
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


/**
 * A simple [Fragment] subclass.
 * Use the [NotesDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        if (args.noteId != 0L) {
            viewBinding.saveIcon.visibility = View.GONE
            observe()
        }
    }

    private fun observe() {
        notesViewModel.liveData.observe(viewLifecycleOwner) { it ->
            existingNote = it.find { it.id == args.noteId }
            if (existingNote != null) populateContent(existingNote!!)
        }
    }

    private fun populateContent(note: Note) {
        noteStatus = NoteStatus.EDITING_NOTE
        viewBinding.noteTitle.setText(note.title)
        viewBinding.noteContents.setText(note.content)
    }

    override fun onResume() {
        super.onResume()
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotesDetailFragment().apply {}
    }
}