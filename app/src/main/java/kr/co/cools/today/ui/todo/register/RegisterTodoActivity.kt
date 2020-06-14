package kr.co.cools.today.ui.todo.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Range
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_register_todo.*
import kotlinx.android.synthetic.main.item_spinner_day.view.*
import kr.co.cools.common.extension.gone
import kr.co.cools.common.extension.visible
import kr.co.cools.today.R
import kr.co.cools.today.ui.utils.WeekNumber

@AndroidEntryPoint
class RegisterTodoActivity: AppCompatActivity() {

    companion object {
        val EXTRA_KEY_DAY_OF_WEEK = "extra_key_day_of_week"

        fun forIntent(context: Context, dayOfWeek: Int): Intent {
            return Intent(context, RegisterTodoActivity::class.java).apply {
                putExtra(EXTRA_KEY_DAY_OF_WEEK , dayOfWeek)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_todo)
    }
}