package kr.co.cools.today.ui.home

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*
import kr.co.cools.today.R
import kr.co.cools.today.ui.utils.WeekNumber


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(
            navController.graph,
            drawer_layout
        )
    }
    private val navController: NavController by lazy { findNavController(R.id.nav_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.job_list_fragment -> {
                    toolbar.title = WeekNumber.getWeekString(context = applicationContext)
                }
                R.id.todo_list_fragment -> {
                    toolbar.title = getString(R.string.todo_of_day)
                }
                R.id.register_todo_fragment -> {
                    toolbar.title = getString(R.string.register_todo)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(drawer_layout)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(Gravity.LEFT)){
            drawer_layout.closeDrawers()
        }else {
            super.onBackPressed()
        }
    }
}
