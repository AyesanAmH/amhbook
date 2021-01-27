package com.aungmoehein.moehein.music

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData

object MusicUtils {
    fun getAllAudio(context: Context):MutableList<Music>{
        val list = mutableListOf<Music>()
        val projection = arrayOf(MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,MediaStore.Audio.AudioColumns.ARTIST)
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null)
        if(cursor != null && cursor.moveToFirst()){
            do {
                val title = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val path = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val artist = cursor.getColumnIndexOrThrow(MediaStore.Audio.ArtistColumns.ARTIST)
                list.add(Music(name = cursor.getString(title),path = cursor.getString(path),artist = cursor.getString(artist)))
            }while (cursor.moveToNext())
        }
        cursor?.close()
        return list
    }

    //happy
    val mutableIndex= MutableLiveData<Int>()
    fun addIndex(index: Int){
        mutableIndex.value = index
    }

    val isPlaying = MutableLiveData<Boolean>()
    fun setPlying(play : Boolean){
        isPlaying.value = play
    }

    val mutablePosition = MutableLiveData<Long>()
    fun addPosition(position : Long){
        mutablePosition.value = position
    }


}