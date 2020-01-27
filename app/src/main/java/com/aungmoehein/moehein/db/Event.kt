package com.aungmoehein.moehein.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "event_table")
data class Event(
    @PrimaryKey(autoGenerate = true)val id:Long=0,
    @ColumnInfo(name = "name") val name:String,
    @ColumnInfo(name = "day") val day:Long,
    @ColumnInfo(name = "date") val date:String,
    @ColumnInfo(name = "time") val time:String,
    @ColumnInfo(name = "mmtime") val mmtime:String,
    @ColumnInfo(name = "place") val place:String,
    @ColumnInfo(name = "strAlarm") val strAlarm:String,
    @ColumnInfo(name = "mlsAlarm") val mlsAlarm:Long,
    @ColumnInfo(name = "setalarm")val setAlarm:Boolean
    ) {
}