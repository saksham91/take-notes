package com.example.takenotes.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.takenotes.R
import com.example.takenotes.databinding.NotesItemBinding
import com.example.takenotes.model.Note

class NotesAdapter(private val onNoteClicked: (Note) -> Unit)
    : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var visibleList: List<Note> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NotesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return visibleList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(visibleList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(notes: List<Note>) {
        visibleList = notes
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: NotesItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.noteTitle.text = note.title
            binding.noteContent.text = note.content
            binding.dateView.text = note.dateModified.toString()
            binding.dateView.isSelected = true
            itemView.setOnClickListener {
                onNoteClicked(note)
            }
        }
    }
}