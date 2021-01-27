package com.aungmoehein.moehein.music
import android.annotation.TargetApi
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.media.AudioAttributesCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import org.jetbrains.anko.AnkoLogger

class AudioFocusWrapper(private val audioAttributesCompat: AudioAttributesCompat,
                        private val audioManager: AudioManager,
                        private val player :SimpleExoPlayer) : ExoPlayer by player , AnkoLogger {

    private var shouldPlayWhenReady = false
    private  val MEDIA_VOLUME_DEFAULT = 1.0f
    private val MEDIA_VOLUME_DUCK =  0.2f

    private val audioFocusListener =
        AudioManager.OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> {
                    if (shouldPlayWhenReady || player.playWhenReady) {
                        player.playWhenReady = true
                        player.volume = MEDIA_VOLUME_DEFAULT
                    }
                    shouldPlayWhenReady = false
                }

                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                    if (player.playWhenReady)
                        player.volume = MEDIA_VOLUME_DUCK
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    shouldPlayWhenReady = player.playWhenReady
                    player.playWhenReady = false
                }

            }
        }

    @get:RequiresApi(Build.VERSION_CODES.O)
    private val audioFocusRequest by lazy { buildFocusRequest() }

    override fun setPlayWhenReady(playWhenReady: Boolean) {
        if (playWhenReady)
            requestAudioFocus()
        else
            abandonAudioFocus()
    }
    private fun requestAudioFocus(){
        val result = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            requestAudioFocusOreo() }
        else{
            @Suppress("deprecation")
            audioManager.requestAudioFocus(audioFocusListener,
                audioAttributesCompat.legacyStreamType,
                AudioManager.AUDIOFOCUS_GAIN) }

        if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            shouldPlayWhenReady = true
            audioFocusListener.onAudioFocusChange(AudioManager.AUDIOFOCUS_GAIN)
        }
    }

    private fun abandonAudioFocus(){
        player.playWhenReady = false
        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.O){
            abandonAudioFocusOreo()
        }else{
            @Suppress("deprecation")
            audioManager.abandonAudioFocus(audioFocusListener)
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun requestAudioFocusOreo() =
        audioManager.requestAudioFocus(audioFocusRequest)

    @TargetApi(Build.VERSION_CODES.O)
    private fun abandonAudioFocusOreo()=
        audioManager.abandonAudioFocusRequest(audioFocusRequest)

    @TargetApi(Build.VERSION_CODES.O)
    private fun buildFocusRequest():AudioFocusRequest =
        AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(
                audioAttributesCompat.unwrap() as AudioAttributes)
            .setOnAudioFocusChangeListener (audioFocusListener)
            .build()
}
