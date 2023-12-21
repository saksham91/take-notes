package com.example.takenotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.takenotes.databinding.FragmentNotesDetailBinding


/**
 * A simple [Fragment] subclass.
 * Use the [NotesDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesDetailFragment : Fragment() {

    private var _viewBinding: FragmentNotesDetailBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentNotesDetailBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.toolbar.setNavigationIcon(com.google.android.material.R.drawable.ic_arrow_back_black_24)
        viewBinding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotesDetailFragment().apply {}
    }
}