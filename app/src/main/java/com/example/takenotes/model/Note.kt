package com.example.takenotes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class Note(@PrimaryKey(autoGenerate = true) val id: Int,
                var title: String,
                var content: String,
                @ColumnInfo(name = "created")var dateCreated: Date,
                @ColumnInfo(name = "modified")var dateModified: Date)
