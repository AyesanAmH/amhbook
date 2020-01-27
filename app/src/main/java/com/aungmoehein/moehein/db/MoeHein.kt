package com.aungmoehein.moehein.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Poem::class,Buy::class,Read::class,Review::class,Event::class,LibraryWriter::class,LibraryBook::class],version = 1)
abstract class MoeHein:RoomDatabase() {
    abstract fun poemDao() : PoemDao
    abstract fun buyDao() : BuyDao
    abstract fun readDao():ReadDao
    abstract fun reviewDao():ReviewDao
    abstract fun eventDao():EventDao
    abstract fun libraryWriterDao() : LibraryWriterDao
    abstract fun libraryBookDao() : LibraryBookDao
    companion object {
        private var INSTANCE: MoeHein? = null
        fun getInstance(context: Context): MoeHein {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MoeHein::class.java,
                    "bookinfo.db"
                ).build()
            }
            return INSTANCE!!
        }


        fun destroyInstance() {
            INSTANCE = null
        }


    }
}