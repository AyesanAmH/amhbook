package com.aungmoehein.moehein.music


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.aungmoehein.moehein.R
import com.google.android.exoplayer2.Player
import kotlinx.android.synthetic.main.fragment_music.*
import kotlinx.android.synthetic.main.fragment_music.playerView
import kotlinx.android.synthetic.main.fragment_music_detail.*
import kotlinx.android.synthetic.main.music_detail_player_layout.*

/**
 * A simple [Fragment] subclass.
 */
class MusicDetailFragment : Fragment() {
    lateinit var connection: ServiceConnection
    var bounded = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerView.showController()

        connection = object : ServiceConnection{
            override fun onServiceDisconnected(p0: ComponentName?) {

            }

            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                val binder = p1 as MusicService.ServiceBinder
                bounded = true
                val player = binder.getExoPlayerInstance()
                playerView.player = player
            }
        }

        activity?.bindService(Intent(context,MusicService::class.java),connection, Context.BIND_AUTO_CREATE)

        music_detail_previous.setOnClickListener {
            connection = object : ServiceConnection {
                override fun onServiceDisconnected(p0: ComponentName?) {

                }

                override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

                    val binder = p1 as MusicService.ServiceBinder
                    bounded = true
                    val player = binder.getExoPlayerInstance()
                    playerView.player = player
                    player?.playWhenReady = true
                    val index = player?.previousWindowIndex
                    if(index != -1)
                        player?.previous()
                }

            }
            activity?.bindService(Intent(context,MusicService::class.java),connection, Context.BIND_AUTO_CREATE)
        }

        music_detail_next.setOnClickListener {
            connection = object : ServiceConnection {
                override fun onServiceDisconnected(p0: ComponentName?) {

                }

                override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

                    val binder = p1 as MusicService.ServiceBinder
                    bounded = true
                    playerView.player = binder.getExoPlayerInstance()
                    val player = binder.getExoPlayerInstance()
                    player?.playWhenReady = true
                    val index = player?.nextWindowIndex
                    if(index != -1)
                        player?.next()
                }

            }
            activity?.bindService(Intent(context,MusicService::class.java),connection, Context.BIND_AUTO_CREATE)
        }

        music_detail_play.setOnClickListener {
            connection = object : ServiceConnection {
                override fun onServiceDisconnected(p0: ComponentName?) {

                }

                override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

                    val binder = p1 as MusicService.ServiceBinder
                    bounded = true
                    val player = binder.getExoPlayerInstance()
                    playerView.player = player
                    player?.playWhenReady = !player?.playWhenReady!!
                }

            }
            activity?.bindService(Intent(context,MusicService::class.java),connection, Context.BIND_AUTO_CREATE)
        }

        music_detail_repeat.setOnClickListener {
            connection = object : ServiceConnection {
                override fun onServiceDisconnected(p0: ComponentName?) {

                }

                override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

                    val binder = p1 as MusicService.ServiceBinder
                    bounded = true
                    val player = binder.getExoPlayerInstance()
                    if(player?.repeatMode == Player.REPEAT_MODE_OFF){
                        player.repeatMode = Player.REPEAT_MODE_ONE
                        music_detail_repeat.setImageResource(R.drawable.ic_repeat_one_black_on_24dp)
                    }else if ( player?.repeatMode == Player.REPEAT_MODE_ONE){
                        player.repeatMode = Player.REPEAT_MODE_OFF
                        music_detail_repeat.setImageResource(R.drawable.ic_repeat_one_black_off_24dp)
                    }

                }

            }
            activity?.bindService(Intent(context,MusicService::class.java),connection, Context.BIND_AUTO_CREATE)
        }

        music_detail_shuffle.setOnClickListener {
            connection = object : ServiceConnection {
                override fun onServiceDisconnected(p0: ComponentName?) {

                }

                override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

                    val binder = p1 as MusicService.ServiceBinder
                    bounded = true
                    val player = binder.getExoPlayerInstance()
                    if (player?.shuffleModeEnabled!!)
                        music_detail_shuffle.setImageResource(R.drawable.ic_shuffle_black_off_24dp)

                    else
                        music_detail_shuffle.setImageResource(R.drawable.ic_shuffle_black_on_24dp)

                    player.shuffleModeEnabled = ! player.shuffleModeEnabled

                }

            }
            activity?.bindService(Intent(context,MusicService::class.java),connection, Context.BIND_AUTO_CREATE)
        }

        MusicUtils.mutableIndex.observe(viewLifecycleOwner, Observer {
            music_detail_name.text = MusicUtils.getAllAudio(context!!)[it].name
            music_detail_artist.text = MusicUtils.getAllAudio(context!!)[it].artist
        })


        MusicUtils.isPlaying.observe(viewLifecycleOwner, Observer {
            if (it)
                music_detail_play.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp)
            else
            {
                music_detail_play.setImageResource(R.drawable.ic_play_circle_filled_black_24dp)
                if (bounded){
                    activity!!.unbindService(connection)
                    bounded = false
                }
            }
        })
    }


}
