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
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.takenotes.NoteStatus
import com.example.takenotes.R
import com.example.takenotes.database.NotesDatabase
import com.example.takenotes.databinding.BottomSheetLayoutBinding
import com.example.takenotes.databinding.FragmentNotesDetailBinding
import com.example.takenotes.model.Note
import com.google.android.material.bottomsheet.BottomSheetDialog

class NotesDetailFragment : Fragment() {

    private var _viewBinding: FragmentNotesDetailBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val notesViewModel: NotesViewModel by viewModels()
    private val args: NotesDetailFragmentArgs by navArgs()
    private var existingNote: Note? = null
    private var noteStatus = NoteStatus.NEW_NOTE
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetBinding: BottomSheetLayoutBinding

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
        initBottomSheet()
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
        viewBinding.bgColorChange.setOnClickListener {
            changeBg()
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

    private fun initBottomSheet() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetBinding = BottomSheetLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
    }

    // TODO convert to recycler adapter
    private fun changeBg() {
        bottomSheetDialog.show()
        val imageContainer = bottomSheetBinding.imagePickerView
        imageContainer.bgBright.setOnClickListener { updateBackground(it) }
        imageContainer.bgWave.setOnClickListener { updateBackground(it) }
        imageContainer.bgConcentricCircle.setOnClickListener { updateBackground(it) }
        imageContainer.bgWaveDark.setOnClickListener { updateBackground(it) }
        imageContainer.blurryBg.setOnClickListener { updateBackground(it) }

        val textContainer = bottomSheetBinding.textColorPickerView
        textContainer.gradient1.setOnClickListener { updateTextColor(it) }
        textContainer.gradient2.setOnClickListener { updateTextColor(it) }
        textContainer.gradient3.setOnClickListener { updateTextColor(it) }
        textContainer.gradient4.setOnClickListener { updateTextColor(it) }
        textContainer.gradient5.setOnClickListener { updateTextColor(it) }
    }

    private fun updateBackground(view: View) {
        val bg = notesViewModel.updateBgImage(view)
        viewBinding.root.setBackgroundResource(bg)
    }

    private fun updateTextColor(view: View) {
        val textColor = notesViewModel.updateTextColor(view)
        viewBinding.noteContents.setTextColor(ContextCompat.getColor(requireContext(), textColor))
        viewBinding.noteTitle.setTextColor(ContextCompat.getColor(requireContext(), textColor))
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotesDetailFragment().apply {}
    }
}