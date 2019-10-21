package com.aungmoehein.moehein.db

import android.content.Context
import android.widget.Toast
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Poem::class,Buy::class],version = 1)
abstract class MoeHein:RoomDatabase() {
    abstract fun poemDao() : PoemDao
    abstract fun buyDao() : BuyDao
    companion object {
        private var INSTANCE: MoeHein? = null
        fun getInstance(context: Context): MoeHein {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MoeHein::class.java,
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