package com.aungmoehein.moehein.music

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aungmoehein.moehein.R
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray

class MusicAdapter(val context: Context,val list: MutableList<Music>):RecyclerView.Adapter<MusicAdapter.MusicHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAdapter.MusicHolder {
        return MusicHolder(LayoutInflater.from(context).inflate(R.layout.music_name,parent,false),context)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MusicAdapter.MusicHolder, position: Int) {
        holder.name.text = list[position].name
        holder.artist.text = list[position].artist
    }


    class MusicHolder(itemView: View,val context: Context)  :RecyclerView.ViewHolder(itemView),View.OnClickListener,Player.EventListener {

        init {
            itemView.setOnClickListener(this)
        }
        val name = itemView.findViewById<TextView>(R.id.music_name)
        val artist = itemView.findViewById<TextView>(R.id.music_artist)
        override fun onClick(p0: View?) {
            val connection = object : ServiceConnection {
                override fun onServiceDisconnected(p0: ComponentName?) {

                }

                override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

                    val binder = p1 as MusicService.ServiceBinder
                    val player = binder.getExoPlayerInstance()!!
                    player.playWhenReady = true
                    player.seekTo(adapterPosition, C.TIME_UNSET)
                    MusicUtils.addIndex(player.currentWindowIndex)
                    player.addListener(object : Player.EventListener{

                        override fun onTracksChanged(
                            trackGroups: TrackGroupArray?,
                            trackSelections: TrackSelectionArray?
                        ) {
                            MusicUtils.addIndex(player.currentWindowIndex)
                        }
                    })

                }

            }

            context.bindService(Intent(context,MusicService::class.java),connection,Context.BIND_AUTO_CREATE)

        }

    }
}