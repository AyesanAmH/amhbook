package com.aungmoehein.moehein.viewmodel

import android.app.Application
import android.os.Bundle
import android.util.Log.i
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poemroomone.db.PoemDb
import com.example.poemroomone.db.Poem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.absoluteValue

class PoemViewModel(application: Application):AndroidViewModel(application) {

    val db = PoemDb.getInstance(application)
    val allPoems: LiveData<List<Poem>> = db.poemDao().getAllPoems()
    private val scope = CoroutineScope(Dispatchers.IO)
    fun insertPoem(poem: Poem) = scope.launch {

        db.poemDao().insertPoem(poem)
    }


}