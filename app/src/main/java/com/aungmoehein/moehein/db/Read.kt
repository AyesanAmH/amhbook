package com.aungmoehein.moehein.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "read_table")
data class Read (
    @PrimaryKey(autoGenerate = true)val id:Long=0,
    @ColumnInfo(name = "title")val title:String,
    @ColumnInfo(name = "writer")val writer:String,
    @ColumnInfo(name = "recom")val recom:String,
    @ColumnInfo(name = "comment")val comment:String

    )