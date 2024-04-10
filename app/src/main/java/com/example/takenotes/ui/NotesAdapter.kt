package com.example.takenotes.ui

import android.annotation.SuppressLint
import android.app.AppOpsManager.OnOpNotedCallback
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.takenotes.R
import com.example.takenotes.model.Note

class NotesAdapter(noteListener: OnNoteListener) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var visibleList: List<Note> = emptyList()
    private var clickListener: OnNoteListener

    init {
        clickListener = noteListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_item, parent, false)
        return ViewHolder(view, clickListener)
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

    inner class ViewHolder(view: View, onNoteListener: OnNoteListener):
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private val noteTitle: TextView
        private val noteContent: TextView
        private val noteModified: TextView
        private val onNoteListener: OnNoteListener

        init {
            noteTitle = view.findViewById(R.id.note_title)
            noteContent = view.findViewById(R.id.note_content)
            noteModified = view.findViewById(R.id.date_view)
            this.onNoteListener = onNoteListener
            view.setOnClickListener(this)
        }

        fun bind(note: Note) {
            noteTitle.text = note.title
            noteContent.text = note.content
            noteModified.text = note.dateModified.toString()
            noteModified.isSelected = true
        }

        override fun onClick(v: View?) {
            onNoteListener.onNoteClick(adapterPosition, visibleList[adapterPosition].id)
        }

    }

    interface OnNoteListener {
        fun onNoteClick(position: Int, id: Long)
    }
}