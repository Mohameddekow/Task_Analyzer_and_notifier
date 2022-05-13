package com.example.taskkeeperandanalyzer.ui



import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.taskkeeperandanalyzer.R
import com.example.taskkeeperandanalyzer.constants.TASK_ROOT_REF
import com.example.taskkeeperandanalyzer.databinding.ActivityMainBinding
import com.example.taskkeeperandanalyzer.notification.*
import com.example.taskkeeperandanalyzer.ui.home.HomeViewModel
import com.example.taskkeeperandanalyzer.utils.Resource
import com.example.taskkeeperandanalyzer.utils.showLongToast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val homeViewModel by viewModels<HomeViewModel>()

    @Inject
    lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //bottom nav background null
        binding.bottomNavView.background = null


        binding.apply {

            val userId = auth.currentUser?.uid
            if (userId != null) {
                homeViewModel.getAllTasks(userId, TASK_ROOT_REF)
            }

        }

        createNotificationChannel()

        scheduleNotification()


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
                    supportActionBar?.hide()

                    bottomNavAppBar.isVisible = false

                }

                R.id.registerFragment -> {
                    toolBar.navigationIcon = null
                    supportActionBar?.hide()

                    bottomNavAppBar.isVisible = false

                }

                R.id.resetPasswordFragment -> {
                    toolBar.navigationIcon = null
                    supportActionBar?.hide()

                    bottomNavAppBar.isVisible = false

                }

                R.id.settingsFragment -> {
                    bottomNavAppBar.isVisible = false

                }

                R.id.profileFragment -> {
                    bottomNavAppBar.isVisible = false
                    supportActionBar?.show()


                }
                R.id.editProfileFragment -> {
                    bottomNavAppBar.isVisible = false

                }
                R.id.currentPasswordFragment -> {
                    bottomNavAppBar.isVisible = false

                }
                R.id.changePasswordFragment -> {
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


//    @RequiresApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleNotification()
    {

        homeViewModel.fetchingTasksState.observe(this){  result ->

            when(result){
                is Resource.Success -> {
                    var title = ""
                    var desc = " "
                    var time: Long = 0

                    for (i in result.data!!){
                         title = i.taskType.toString()
                         desc = i.desc.toString()

                        val currentTime = System.currentTimeMillis()

                        time = if (i.creationTimeMs!! > currentTime){
                            i.creationTimeMs
                        }else{
                            0
                        }
                    }

                    val intent = Intent(applicationContext, Notification::class.java)

                    intent.putExtra(titleExtra, title)
                    intent.putExtra(messageExtra, desc)


                    val pendingIntent = PendingIntent.getBroadcast(
                        applicationContext,
                        notificationID,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        time,
                        pendingIntent
                    )
                }
                is Resource.Error -> {
                    showLongToast(this, result.error.toString())
                }
                else -> {
                    //show nothing
                }
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel()
    {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.fragmentContainerView)
        return navController.navigateUp()
    }
}