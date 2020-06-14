package kr.co.cools.today.ui.job.list

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_joblist.*
import kr.co.cools.common.logger.Logger
import kr.co.cools.today.R
import kr.co.cools.today.ui.todo.list.TodoListActivity

@AndroidEntryPoint
class JobListActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joblist)

        nav_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.setting -> {
                    startActivity(Intent(this, TodoListActivity::class.java))
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Logger.v("onOptionsItem.${item}")
        when(item.itemId){
            android.R.id.home -> {
                drawableLayout.openDrawer(Gravity.LEFT)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(drawableLayout.isDrawerOpen(Gravity.LEFT)){
            drawableLayout.closeDrawer(Gravity.LEFT)
        }
        else {
            super.onBackPressed()
        }
    }


}