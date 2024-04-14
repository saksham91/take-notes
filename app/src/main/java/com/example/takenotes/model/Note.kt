package com.example.takenotes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "notes")
data class Note(@PrimaryKey(autoGenerate = true) val id: Long = 0L,
                var title: String,
                var content: String,
                @ColumnInfo(name = "created")
                var dateCreated: String = Date().toString(),
                @ColumnInfo(name = "modified")
                var dateModified: String = Date().toString(),
                @ColumnInfo(name = "background", defaultValue = "0")
                var noteBg: Int,
                @ColumnInfo(name = "text_color", defaultValue = "0")
                var textColor: Int,
                @ColumnInfo(name = "item_state", defaultValue = "0")
                var noteItemState: Boolean)
