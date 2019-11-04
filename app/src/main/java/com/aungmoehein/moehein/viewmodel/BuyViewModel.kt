package com.aungmoehein.moehein.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.aungmoehein.moehein.db.Buy
import com.aungmoehein.moehein.db.MoeHein

class BuyViewModel(application: Application) :AndroidViewModel(application) {
    val db = MoeHein.getInstance(application)
    val allbuy = db.buyDao().getAllBuy()
    val allbuywriter = db.buyDao().getAllBuyWriter()
}