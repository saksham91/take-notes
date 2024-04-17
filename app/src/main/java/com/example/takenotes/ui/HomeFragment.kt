package com.example.takenotes.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.takenotes.R
import com.example.takenotes.database.NotesDatabase
import com.example.takenotes.databinding.FragmentHomeBinding
import com.example.takenotes.model.Note

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val notesViewModel: NotesViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private var adapter: NotesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.addNoteButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_notesDetailFragment)
        }
        val dao = NotesDatabase.getNotesDatabase(activity as Activity).notesDao()
        notesViewModel.initialize(dao)
        initRecyclerView()
        observe()
        listener()
    }

    private fun initRecyclerView() {
        adapter = NotesAdapter(::onNoteClicked, requireContext())
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun observe() {
        notesViewModel.liveData.observe(viewLifecycleOwner) {
            val sortedList = it.sortedByDescending { item -> item.dateCreated }
            adapter?.updateData(sortedList)
        }
    }

    private fun listener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter?.filterNotes(newText)
                return true
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    private fun onNoteClicked(note: Note) {
        val action = HomeFragmentDirections.actionHomeFragmentToNotesDetailFragment(note.id)
        findNavController().navigate(action)
    }
}