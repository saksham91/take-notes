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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.takenotes.R
import com.example.takenotes.database.NotesDatabase
import com.example.takenotes.databinding.FragmentNotesDetailBinding


/**
 * A simple [Fragment] subclass.
 * Use the [NotesDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesDetailFragment : Fragment() {

    private var _viewBinding: FragmentNotesDetailBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val notesViewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        viewBinding.toolbar.setNavigationIcon(R.drawable.ic_back_navigation)
        viewBinding.toolbar.inflateMenu(R.menu.menu)
        viewBinding.toolbar.setOnMenuItemClickListener{
            onOptionsItemSelected(it)
        }
        viewBinding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.done -> {
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
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNote() {
        val note = viewBinding.noteContents.text.toString()
        val title = viewBinding.noteTitle.text.toString()
        notesViewModel.insertNote(note, title)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotesDetailFragment().apply {}
    }
}