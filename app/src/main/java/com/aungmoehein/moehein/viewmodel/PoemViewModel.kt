package com.aungmoehein.moehein.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.poemroomone.db.K5L
import com.example.poemroomone.db.Poem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PoemViewModel(application: Application):AndroidViewModel(application) {

    val db = K5L.getInstance(application)
    val allPoems:LiveData<List<Poem>> = db.poemDao().getAllPoems()
    private val scope = CoroutineScope(Dispatchers.Main)

    fun insertPoem(poem: Poem) = scope.launch {

        db.poemDao().insertPoem(poem)
    }

    fun getPoemById(id:Long) = scope.launch {
        db.poemDao().getPoemById(id)
    }
}