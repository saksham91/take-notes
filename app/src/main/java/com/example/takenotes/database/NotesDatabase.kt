package com.example.takenotes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.takenotes.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun notesDao(): NoteDAO

    companion object {

        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getNotesDatabase(context: Context) : NotesDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        NotesDatabase::class.java,
                        "notesDB")
                        .fallbackToDestructiveMigration()   //TODO Add migration code
                        .build()
                }
            }
            return INSTANCE!!
        }

    }

}