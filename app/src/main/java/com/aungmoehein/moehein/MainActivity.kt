package com.aungmoehein.moehein

import android.os.Bundle
import android.widget.MediaController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.poem_top_view.*
import me.myatminsoe.mdetect.MDetect
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import kotlinx.android.synthetic.main.fragment_poem_list.*

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //MDetect
        MDetect.init(this)
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
        findNavController(this, R.id.host_fragment).navigateUp()


}
