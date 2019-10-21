package com.aungmoehein.moehein.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.w3c.dom.Comment

@Entity(tableName ="buy_table")
data class Buy (
    @PrimaryKey(autoGenerate = true)val id:Long=0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "writer") val writer:String,
    @ColumnInfo(name = "quantity") val quantity:Long,
    @ColumnInfo(name = "comment") val comment: String)
