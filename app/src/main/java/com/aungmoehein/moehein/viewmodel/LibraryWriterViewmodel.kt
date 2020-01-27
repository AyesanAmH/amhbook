package com.aungmoehein.moehein.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aungmoehein.moehein.db.MoeHein

class LibraryWriterViewmodel(application: Application) : AndroidViewModel(application) {
    val db = MoeHein.getInstance(application)
    val allwriters = db.libraryWriterDao().getAllWriters()
}