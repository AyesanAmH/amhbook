package com.aungmoehein.moehein

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.text.Layout
import android.util.Log.i
import android.view.*
import android.view.GestureDetector
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.aungmoehein.moehein.music.MusicService
import com.aungmoehein.moehein.music.MusicUtils
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.button_read.*
import kotlinx.android.synthetic.main.button_event.*
import kotlinx.android.synthetic.main.button_music.*
import kotlinx.android.synthetic.main.button_poem.*
import kotlinx.android.synthetic.main.button_review.*
import kotlinx.android.synthetic.main.button_buy.*
import kotlinx.android.synthetic.main.music_home_layout.*
import kotlinx.android.synthetic.main.music_player.*
import kotlinx.android.synthetic.main.poem_top_view.*
import kotlinx.android.synthetic.main.poem_top_view.poem_title
import me.myatminsoe.mdetect.MDetect
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {

    lateinit var activity: MainActivity

    var bounded = false
    lateinit var connection: ServiceConnection
    var exoPlayer: ExoPlayer? = null
    var position: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setText()



        //random number
        activity = context as MainActivity
        val ran = activity.random
        if(ran.toInt() != 0){
            poem_title.text = MDetect.getText(activity.ptitle)
            poem_content.text = MDetect.getText(activity.pcontext)
            poem_top_writer.text = MDetect.getText(activity.pwriter)

            val count = activity.pcontext.lines().count()
            val lines = activity.pcontext
            var content = "$lines"
            if(count< 5  ){
                when(count){
                    1 -> content = "\n$lines\n\n"
                    2 -> content = "\n$lines\n"
                    3 -> content = "$lines\n"
                }
                poem_content.text = MDetect.getText(content)
//                poem_content.setExpand(true)
                poem_detail.visibility = View.GONE
            }
            else
                poem_detail.text = MDetect.getText(poem_detail.text.toString())
        }
        else{
//            poem_content.setExpand(false)
            poem_title.text = MDetect.getText(poem_title.text.toString())
            poem_content.text = MDetect.getText(poem_content.text.toString())
            poem_top_writer.text = MDetect.getText(poem_top_writer.text.toString())
            poem_detail.text = MDetect.getText(poem_detail.text.toString())

        }


        //poem action
        poem_btn.setOnClickListener {
            val poemList = HomeFragmentDirections.poemListAction()
            Navigation.findNavController(it).navigate(poemList)
        }


        //buy action
        buy_btn.setOnClickListener {
            val buy = HomeFragmentDirections.buyAction()
            Navigation.findNavController(it).navigate(buy)
        }


        //read action
        read_btn.setOnClickListener {
            val read = HomeFragmentDirections.readAction()
            Navigation.findNavController(it).navigate(read)
        }


        review_btn.setOnClickListener{
            val action = HomeFragmentDirections.reviewAction()
            Navigation.findNavController(it).navigate(action)
        }


        event_btn.setOnClickListener {
            val action = HomeFragmentDirections.eventAction()
            Navigation.findNavController(it).navigate(action)
        }

        music_btn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                runTimePermissions()
            else{
                val action = HomeFragmentDirections.musicAction()
                Navigation.findNavController(it).navigate(action)
            }
        }


        playerView.showController()

        music_home_previous.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                runTimePermissions()
            else{
                val music = MusicUtils.getAllAudio(requireContext()).isNotEmpty()
                if(music){
                    connection = object : ServiceConnection {
                        override fun onServiceDisconnected(p0: ComponentName?) {

                        }

                        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

                            val binder = p1 as MusicService.ServiceBinder
                            val player = binder.getExoPlayerInstance()
                            bounded = true
                            playerView.player = player
                            exoPlayer = player
                            player?.playWhenReady = true
                            val index = player?.previousWindowIndex
                            if (index != -1)
                                player?.previous()
                        }

                    }
                    activity.bindService(
                        Intent(context, MusicService::class.java),
                        connection,
                        Context.BIND_AUTO_CREATE
                    )
                }

            }

        }


        music_home_next.setOnClickListener {

            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                runTimePermissions()
            else {
                val music = MusicUtils.getAllAudio(requireContext()).isNotEmpty()
                if(music){
                    connection = object : ServiceConnection {
                        override fun onServiceDisconnected(p0: ComponentName?) {

                        }

                        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

                            val binder = p1 as MusicService.ServiceBinder
                            val player = binder.getExoPlayerInstance()
                            bounded = true
                            playerView.player = player
                            exoPlayer = player
                            player?.playWhenReady = true
                            val index = player?.nextWindowIndex
                            if (index != -1)
                                player?.next()
                        }

                    }
                    activity.bindService(
                        Intent(context, MusicService::class.java),
                        connection,
                        Context.BIND_AUTO_CREATE
                    )
                }
            }

        }

        music_home_play.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                runTimePermissions()
            else {
                val music = MusicUtils.getAllAudio(requireContext()).isNotEmpty()
                if(music){
                    connection = object : ServiceConnection {
                        override fun onServiceDisconnected(p0: ComponentName?) {

                        }

                        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

                            val binder = p1 as MusicService.ServiceBinder
                            val player = binder.getExoPlayerInstance()
                            bounded = true
                            exoPlayer = player
                            playerView.player = player
                            player?.playWhenReady = !player?.playWhenReady!!
                        }

                    }
                    activity.bindService(
                        Intent(context, MusicService::class.java),
                        connection,
                        Context.BIND_AUTO_CREATE
                    )
                }
            }

        }


        MusicUtils.mutableIndex.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            song_name.text = MusicUtils.getAllAudio(requireContext())[it].name
        })


        MusicUtils.isPlaying.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                music_home_play.setImageResource(R.drawable.ic_pause_circle_filled_black_home_24dp)
                Util.startForegroundService(context, Intent(context, MusicService::class.java))
            } else {

                music_home_play.setImageResource(R.drawable.ic_play_circle_filled_black_home_24dp)
                activity.stopService(Intent(context, MusicService::class.java))
                if (bounded) {
                    position = exoPlayer!!.currentPosition
                    MusicUtils.addPosition(position)
                    activity.unbindService(connection)
                    bounded = false
                }
            }
        })


        //Expandable Text
        poem_detail.setOnClickListener {
           expand()
        }



//        //double touch
//        val detector = GestureDetector(context,DoubleTapDetector(::expand))
//        val touchListener = View.OnTouchListener { v, event ->
//            detector.onTouchEvent(event)
//        }



//        poem_group.setAllOnTouchlistener(touchListener)

    }




    fun Group.setAllOnTouchlistener(listener: View.OnTouchListener?){
        referencedIds.forEach { id ->
            rootView.findViewById<View>(id).setOnTouchListener(listener)
        }
    }




    fun setText() {
        //Set text to six buttom
        poem_btn_text.text = MDetect.getText("ကဗျာ၊စာစု")
        love_btn_text.text = MDetect.getText("သို့")
        buy_btn_text.text = MDetect.getText("ဝယ်မည်")
        music_btn_text.text = MDetect.getText("အသံ")
        review_btn_text.text = MDetect.getText("သုံးသပ်ချက်")
        book_btn_text.text = MDetect.getText("ဖတ်မည်")
    }



    fun expand() {
        if (poem_detail.visibility != View.GONE) {
            if (poem_detail.text.equals(MDetect.getText("မူလ"))) {
//                poem_content.setExpand(false)
                poem_content.collapse()
                poem_detail.text = MDetect.getText("အပြည့်အစုံ")
            } else {
//                poem_content.setExpand(true)
                poem_content.toggle()
                poem_detail.text = MDetect.getText("မူလ")
            }
        }
    }


    fun runTimePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        }
    }
}
