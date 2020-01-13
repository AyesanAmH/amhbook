package com.aungmoehein.moehein.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aungmoehein.moehein.db.MoeHein
import me.myatminsoe.mdetect.MDetect

class ReviewViewModel(application:Application):AndroidViewModel(application) {
    val db = MoeHein.getInstance(application)
    val allreviews = db.reviewDao().getAllReviews()
    val allnames = db.reviewDao().getAllReviewName()
    val allwriters = db.reviewDao().getAllReviewWriter()
    val allcats = db.reviewDao().getAllReviewCat()
    val allfav = db.reviewDao().getAllReviewFav(true)
}