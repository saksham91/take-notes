package com.example.takenotes.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.takenotes.R
import com.example.takenotes.model.Note

class NotesAdapter() : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var visibleList: List<Note> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_item, parent, false)
        return ViewHolder(view)
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


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val noteTitle: TextView
        private val noteContent: TextView
        private val noteModified: TextView

        init {
            noteTitle = view.findViewById(R.id.note_title)
            noteContent = view.findViewById(R.id.note_content)
            noteModified = view.findViewById(R.id.date_view)
        }

        fun bind(note: Note) {
            noteTitle.text = note.title
            noteContent.text = note.content
            noteModified.text = note.dateModified.toString()
            noteModified.isSelected = true
        }

    }
}