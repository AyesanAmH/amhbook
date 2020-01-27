package com.aungmoehein.moehein.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library_writer")
data class LibraryWriter(
    @PrimaryKey(autoGenerate = true)val id:Long=0,
    @ColumnInfo(name = "name")val name:String,
    @ColumnInfo(name = "qty")val qty:Int) {
}