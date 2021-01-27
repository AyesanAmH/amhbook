package com.aungmoehein.moehein.music
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.Navigation
import com.aungmoehein.moehein.MainActivity
import com.aungmoehein.moehein.R

import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class DescriptionAdapter(val context: Context):PlayerNotificationManager.MediaDescriptionAdapter {
    val music = MusicUtils.getAllAudio(context)


    override fun createCurrentContentIntent(player: Player?): PendingIntent? {
        return pendingIntent(context)
    }

    override fun getCurrentContentText(player: Player?): String? {
        return music[player!!.currentWindowIndex].artist
    }

    override fun getCurrentContentTitle(player: Player?): String {
        return music[player!!.currentWindowIndex].name
    }

    override fun getCurrentLargeIcon(
        player: Player?,
        callback: PlayerNotificationManager.BitmapCallback?
    ): Bitmap? {
        return getBitmapFromVectorDrawable(context, R.drawable.ic_music_note_black_24dp)
    }

    @MainThread
    private fun getBitmapFromVectorDrawable(context: Context, @DrawableRes drawableId: Int): Bitmap? {
        return ContextCompat.getDrawable(context, drawableId)?.let {
            val drawable = DrawableCompat.wrap(it).mutate()

            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            bitmap
        }
    }

    fun pendingIntent(context: Context):PendingIntent{
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_bottom)
            .setDestination(R.id.musicDetailFragment)
            .createPendingIntent()
    }


}