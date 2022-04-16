package com.example.taskkeeperandanalyzer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.taskkeeperandanalyzer.R
import com.example.taskkeeperandanalyzer.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //bottom nav background null
        binding.bottomNavView.background = null

        //set up the toolbar
        val toolBar = binding.mainActivityToolbar
        setSupportActionBar(toolBar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        //set up bottom nav with nav controller
        binding.bottomNavView.setupWithNavController(navController)


        //hiding the navController in the splash , login and register
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            //initialize bottom nav bar
            val bottomNavAppBar = binding.bottomNavCardView

            when (destination.id) {
                R.id.splashFagment -> {
                    supportActionBar?.hide()
                    toolBar.navigationIcon = null
                    bottomNavAppBar.isVisible = false

                }


                R.id.homeFragment -> {
                    toolBar.navigationIcon = null
                    supportActionBar?.show()

                    bottomNavAppBar.isVisible = true

                }

                R.id.loginFragment -> {
                    toolBar.navigationIcon = null
                    supportActionBar?.show()

                    bottomNavAppBar.isVisible = false

                }

                R.id.registerFragment -> {
                    toolBar.navigationIcon = null
                    supportActionBar?.show()

                    bottomNavAppBar.isVisible = false

                }

                R.id.settingsFragment -> {
                    bottomNavAppBar.isVisible = false

                }

                R.id.profileFragment -> {
                    bottomNavAppBar.isVisible = false

                }
                R.id.addTaskFragment -> {
                    bottomNavAppBar.isVisible = false

                }

                else -> {
                    //toolBar.navigationIcon = null //hiding the back button from home. login , registration screen
                    supportActionBar?.show()

                    //hide bottom app(main bar of the bottom nav) by default and show only in some destination above
                    bottomNavAppBar.isVisible = true
                }
            }
        }



    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.fragmentContainerView)
        return navController.navigateUp()
    }
}