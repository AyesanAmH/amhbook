package com.aungmoehein.moehein.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.aungmoehein.moehein.db.MoeHein
import com.aungmoehein.moehein.db.Poem

class PoemViewModel(application: Application):AndroidViewModel(application) {
    val db = MoeHein.getInstance(application)
    val allPoems: LiveData<List<Poem>> = db.poemDao().getAllPoems()
}