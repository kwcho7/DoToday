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
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_register_todo.*
import kotlinx.android.synthetic.main.fragment_register_todo.*
import kotlinx.android.synthetic.main.item_spinner_day.view.*
import kr.co.cools.common.extension.gone
import kr.co.cools.common.extension.visible
import kr.co.cools.today.R
import kr.co.cools.today.ui.utils.WeekNumber

@Keep
@AndroidEntryPoint
class RegisterTodoFragment: Fragment() {

    private val viewModel: RegisterTodoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val dayOfWeek = activity?.intent?.getIntExtra(RegisterTodoActivity.EXTRA_KEY_DAY_OF_WEEK, -1)


        initViewModel()
        dayOfWeek?.let {
            initDayOfWeekSpinner(it)
        }

        initButtonAction()
    }

    private fun initViewModel() {
        viewModel.observer(this, Observer {
            it?.apply {
                onChangeViewState(this)
            }
        })
    }

    private fun onChangeViewState(viewState: RegisterTodoViewModel.ViewState) {
        when(viewState){
            is RegisterTodoViewModel.ViewState.ProgressViewState -> {
                if(viewState.isShow){
                    progressBar2.visible()
                }
                else {
                    progressBar2.gone()
                }
            }
            RegisterTodoViewModel.ViewState.AddCompleteViewState -> {
                activity?.finish()
            }
            RegisterTodoViewModel.ViewState.EmptyTitleViewState -> {
                showEmptyTitleMessage()
            }
        }
    }

    private fun showEmptyTitleMessage() {
        Toast.makeText(context, getString(R.string.register_todo_empty_title), Toast.LENGTH_SHORT).show()
    }

    private fun initDayOfWeekSpinner(dayOfWeek: Int) {
        context?.let {
            spinner.adapter = WeekSpinnerAdapter(it)
            if(Range(0, 6).contains(dayOfWeek)){
                spinner.setSelection(dayOfWeek)
            }
        }
    }

    private fun initButtonAction() {
        completeButton.setOnClickListener {
            viewModel.dispatch(
                RegisterTodoViewModel.Action.CompleteAction(titleEditText.text.toString(), descEditText.text.toString(), (spinner.selectedItem as DayOfWeek).number)
            )
        }
    }


    data class DayOfWeek(val context: Context, val number: Int, val text: String = WeekNumber.getWeekString(context, number))

    class WeekSpinnerAdapter(val context: Context): BaseAdapter() {

        private val list = listOf(
            DayOfWeek(context, kr.co.cools.today.repo.entities.DayOfWeek.MON.valueOfDay),
            DayOfWeek(context, kr.co.cools.today.repo.entities.DayOfWeek.TUE.valueOfDay),
            DayOfWeek(context, kr.co.cools.today.repo.entities.DayOfWeek.WED.valueOfDay),
            DayOfWeek(context, kr.co.cools.today.repo.entities.DayOfWeek.THU.valueOfDay),
            DayOfWeek(context, kr.co.cools.today.repo.entities.DayOfWeek.FRI.valueOfDay),
            DayOfWeek(context, kr.co.cools.today.repo.entities.DayOfWeek.SAT.valueOfDay),
            DayOfWeek(context, kr.co.cools.today.repo.entities.DayOfWeek.SUN.valueOfDay)
        )

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val resultView: View
            if(convertView == null){
                resultView = LayoutInflater.from(parent!!.context).inflate(R.layout.item_spinner_day, parent, false)
            }
            else {
                resultView = convertView
            }
            resultView.titleTextView.text = list[position].text
            return resultView
        }

        override fun getItem(position: Int): DayOfWeek {
            return list[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return list.size
        }
    }
}