package com.aungmoehein.moehein

import android.os.Bundle
import android.widget.MediaController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.poem_top_view.*
import me.myatminsoe.mdetect.MDetect

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //MDetect
        MDetect.init(this)
//        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navController = Navigation.findNavController(this,R.id.host_fragment)
        nav_view.let {
            NavigationUI.setupWithNavController(it,navController)
        }

    }

}
