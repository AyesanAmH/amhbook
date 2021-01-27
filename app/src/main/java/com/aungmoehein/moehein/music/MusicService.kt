package com.aungmoehein.moehein.music

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.media.AudioAttributesCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import java.io.File

class MusicService : Service(),Player.EventListener {
    var index = 0
    var positiion = 0
    var player: ExoPlayer? = null
    lateinit var playerNotificationManager: PlayerNotificationManager
    lateinit var mediaSessionCompat: MediaSessionCompat
    lateinit var mediaSessionConnector: MediaSessionConnector
    lateinit var mediaSource : ConcatenatingMediaSource
    lateinit var allmedia : MutableList<Music>

    @SuppressLint("ResourceType")
    override fun onCreate() {
        super.onCreate()

        val context = this

        val audioManger = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val audioAttributes = AudioAttributesCompat.Builder()
            .setContentType(AudioAttributesCompat.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributesCompat.USAGE_MEDIA)
            .build()
        player = AudioFocusWrapper(
            audioAttributes,
            audioManger,
            ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
        )

        mediaSource = ConcatenatingMediaSource()
        allmedia = MusicUtils.getAllAudio(context)

        val PLAYBACK_CHANNEL_ID = "playback_channel"
        val PLAYBACK_NOTIFICATION_ID = 1
        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            PLAYBACK_CHANNEL_ID,
            2,
            PLAYBACK_NOTIFICATION_ID,
            DescriptionAdapter(context)
        )


        playerNotificationManager.setNotificationListener(object : PlayerNotificationManager.NotificationListener {
            override fun onNotificationStarted(
                notificationId: Int,
                notification: Notification?
            ) {
                startForeground(notificationId, notification)
            }


            override fun onNotificationCancelled(notificationId: Int) {
                stopSelf()
            }
        })

        player?.prepare(buildMediaSource())
        player?.addListener(this)


        val index = MusicUtils.mutableIndex.value
        val  position = MusicUtils.mutablePosition.value
        if(index == null )
            player?.seekTo(0,0)
        else
            if(position == null)
                player?.seekTo(index,C.TIME_UNSET)
            else
                player?.seekTo(index,position)

        playerNotificationManager.setPlayer(player)
        playerNotificationManager.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        playerNotificationManager.setUseNavigationActionsInCompactView(true)
        playerNotificationManager.setUseChronometer(false)




        mediaSessionCompat = MediaSessionCompat(context,packageName)
        mediaSessionCompat.isActive = true
        playerNotificationManager.setMediaSessionToken(mediaSessionCompat.sessionToken)

        mediaSessionConnector = MediaSessionConnector(mediaSessionCompat)
        mediaSessionConnector.setPlayer(player)

        MusicUtils.setPlying(false)

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSessionCompat.release()
        mediaSessionConnector.setPlayer(null)
        playerNotificationManager.setPlayer(null)
        player?.release()
        player = null
    }



    fun createExtractorMediaSource(uri: Uri) : MediaSource {
        return ExtractorMediaSource.Factory(
            DefaultDataSourceFactory(this,"asp")
        )
            .createMediaSource(uri)
    }


    //u raise me up
    override fun onLoadingChanged(isLoading: Boolean) {
        val index = player?.currentWindowIndex
        val music = allmedia[index!!]
        val all = MusicUtils.getAllAudio(applicationContext)
       if(!all.contains(music)){
           mediaSource.removeMediaSource(index)
           allmedia.removeAt(index)
       }

    }

    override fun onTracksChanged(
        trackGroups: TrackGroupArray?,
        trackSelections: TrackSelectionArray?
    ) {
        MusicUtils.addIndex(player?.currentWindowIndex!!)
    }

    fun buildMediaSource(): MediaSource {
        allmedia.forEach {
            mediaSource.addMediaSource(
                createExtractorMediaSource(Uri.fromFile(File(it.path)))
            )
        }


        return  mediaSource
    }
    override fun onBind(p0: Intent?): IBinder? {
        return ServiceBinder()
    }


    inner class ServiceBinder : Binder(){
        fun getExoPlayerInstance() = player
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        if(isPlaying)
            MusicUtils.setPlying(isPlaying)
        else
            MusicUtils.setPlying(isPlaying)
    }

}