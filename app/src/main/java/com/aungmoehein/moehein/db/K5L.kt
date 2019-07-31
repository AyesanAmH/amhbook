package com.example.poemroomone.db

import android.content.Context
import android.widget.Toast
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Poem::class],version = 1)
abstract class K5L:RoomDatabase() {
    abstract fun poemDao() : PoemDao
    companion object {
        private var INSTANCE: K5L? = null
        fun getInstance(context: Context): K5L {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    K5L::class.java,
                    "test.db"
                ).build()
                Toast.makeText(context,"Database Created!!!",Toast.LENGTH_SHORT).show()
            }
            return INSTANCE!!
        }


        fun destroyInstance() {
            INSTANCE = null
        }


    }
}