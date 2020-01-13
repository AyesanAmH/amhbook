package com.aungmoehein.moehein

import android.util.Log.i
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast

class DoubleTapDetector(val expand : () -> Unit) : GestureDetector.SimpleOnGestureListener() {
    override fun onDoubleTap(e: MotionEvent?): Boolean {
        expand()
        return true
    }
}