package com.aungmoehein.moehein

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import me.myatminsoe.mdetect.MDetect
import androidx.navigation.fragment.NavHostFragment
import com.aungmoehein.moehein.db.MoeHein
import kotlinx.coroutines.*
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.os.Build
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    lateinit var ptitle: String
    lateinit var pcontext : String
    lateinit var pwriter : String
    var random : Long =0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //MDetect
        MDetect.init(this)

        setSupportActionBar(toolbar)
        val elevation = 0
        supportActionBar!!.elevation = elevation.toFloat()

        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val db = MoeHein.getInstance(applicationContext)
            val list = db.poemDao().getAllId()
            if(list.isNotEmpty()){
                random = list.random()
                ptitle = db.poemDao().getPoemById(random).title
                pcontext = db.poemDao().getPoemById(random).context
                pwriter = db.poemDao().getPoemById(random).writer
            }

        }



        //toolbar
        NavigationUI.setupActionBarWithNavController(this, NavHostFragment.findNavController(host_fragment))


        navController = Navigation.findNavController(this,R.id.host_fragment)

        nav_view.let {
            NavigationUI.setupWithNavController(it,navController)
            onSupportNavigateUp()
        }

    }

    //toolbar action
    override fun onSupportNavigateUp() =
        navController.navigateUp()

}
