package com.aungmoehein.moehein.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aungmoehein.moehein.db.MoeHein

class LibraryBookViewModel (application: Application) : AndroidViewModel(application) {
    val db = MoeHein.getInstance(application)
    val allbooks = db.libraryBookDao().getAllBooks()
}