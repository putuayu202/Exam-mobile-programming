package com.example.motionfinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentLayout = findViewById<FragmentContainerView>(R.id.main_fragment_container)
        val bottomNavigation =findViewById<BottomNavigationView>(R.id.main_nav_bottom)

        bottomNavigation.setupWithNavController(fragmentLayout.findNavController())

    }
}