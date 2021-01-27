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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.aungmoehein.moehein.R
import kotlinx.android.synthetic.main.fragment_music.*
import kotlinx.android.synthetic.main.music_recycler_layout.*
import org.jetbrains.anko.support.v4.act

/**
 * A simple [Fragment] subclass.
 */
class MusicFragment : Fragment() {
    lateinit var connection: ServiceConnection
    var bounded = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val music = MusicUtils.getAllAudio(context!!).isNotEmpty()

        playerView.showController()

        val adapter = MusicAdapter(context!!,MusicUtils.getAllAudio(context!!))
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context)

        recycler_song_detail.setOnClickListener {
            if(music){
                val action = MusicFragmentDirections.detailAction()
                Navigation.findNavController(it).navigate(action)
            }

        }


        music_recycler_play.setOnClickListener {
            if(music){
                connection = object : ServiceConnection {
                    override fun onServiceDisconnected(p0: ComponentName?) {

                    }

                    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

                        val binder = p1 as MusicService.ServiceBinder
                        val player = binder.getExoPlayerInstance()
                        bounded = true
                        playerView.player = player
                        player?.playWhenReady = !player?.playWhenReady!!
                    }

                }
                activity?.bindService(Intent(context,MusicService::class.java),connection, Context.BIND_AUTO_CREATE)
            }

        }

        MusicUtils.mutableIndex.observe(viewLifecycleOwner, Observer {
            recycler_song_name.text = MusicUtils.getAllAudio(context!!)[it].name
            recycler_song_artist.text = MusicUtils.getAllAudio(context!!)[it].artist
        })

        MusicUtils.isPlaying.observe(viewLifecycleOwner, Observer {
            if (it)
                music_recycler_play.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp)
            else{
                music_recycler_play.setImageResource(R.drawable.ic_play_circle_filled_black_24dp)
                if(bounded){
                    activity!!.unbindService(connection)
                    bounded = false
                }
            }
        })
    }


}
