package com.aungmoehein.moehein.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aungmoehein.moehein.db.MoeHein
class ReadViewModel(application: Application):AndroidViewModel(application) {
    val db = MoeHein.getInstance(application)
    val allread = db.readDao().getAllRead()
    val allreadwriter = db.readDao().getAllReadWriter()
    val allreadrecom = db.readDao().getAllReadRecom()
}