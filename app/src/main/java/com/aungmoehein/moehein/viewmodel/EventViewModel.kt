package com.aungmoehein.moehein.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aungmoehein.moehein.db.MoeHein

class EventViewModel(application: Application) : AndroidViewModel(application) {
    val db = MoeHein.getInstance(application)
    val allevents = db.eventDao().getAllEvent()
}