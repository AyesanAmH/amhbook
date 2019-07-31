package com.example.poemroomone.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poem_table")
data class Poem(
    @PrimaryKey(autoGenerate = true)val id:Long=0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "context") val context: String,
    @ColumnInfo(name = "writer") val writer: String)