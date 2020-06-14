package kr.co.cools.today.ui.todo.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_todo_list.*
import kotlinx.android.synthetic.main.fragment_todo_list.*
import kotlinx.android.synthetic.main.item_todo_content.view.*
import kotlinx.android.synthetic.main.item_todo_header.view.*
import kr.co.cools.common.extension.gone
import kr.co.cools.common.extension.visible
import kr.co.cools.design.deco.HeaderDecoration
import kr.co.cools.today.R
import kr.co.cools.today.ui.todo.register.RegisterTodoActivity
import kr.co.cools.today.ui.utils.WeekNumber

@Keep
@AndroidEntryPoint
class TodoListFragment: Fragment() {

    private val viewModel: TodoListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        initRecyclerView()
        initButtonAction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeObservers(this)
        todoRecyclerView.removeItemDecoration(headerDecoration)
    }

    // ViewModel 초기화
    private fun initViewModel() {
        viewModel.observer(this, Observer{
            it?.apply {
                onViewStateChanged(this)
            }
        })
    }

    // List 초기화
    private fun initRecyclerView() {
        todoRecyclerView.apply {
            adapter = TodoAdapter(listOf())
            layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            )
            addItemDecoration(headerDecoration)
        }
    }

    // Button Action 초기화
    private fun initButtonAction() {
        floatingActionButton.setOnClickListener {
            startActivity(Intent(context, RegisterTodoActivity::class.java))
        }
    }

    private fun onViewStateChanged(todoListViewModelState: TodoListViewModel.TodoListViewModelState) {
        when(todoListViewModelState){
            is TodoListViewModel.TodoListViewModelState.UpdateTodoList -> {
                onChangeTodoList(todoList = todoListViewModelState.todoListModel)
                emptyTodoTextView.gone()
            }
            TodoListViewModel.TodoListViewModelState.EmptyTodoList ->{
                emptyTodoTextView.visible()
            }
        }
    }

    private fun onChangeTodoList(todoList: List<TodoListViewModel.TodoModel>){
        (todoRecyclerView.adapter as TodoAdapter).apply {
            this.todoList = todoList
            this.notifyDataSetChanged()
        }
    }

    abstract class TodoViewHolder<T: TodoListViewModel.TodoModel>(val view: View): RecyclerView.ViewHolder(view) {
        abstract fun bind(todoModel: T)
    }

    inner class TodoContentViewHolder(val viewGroup: ViewGroup): TodoViewHolder<TodoListViewModel.TodoModelContent>(viewGroup){
        override fun bind(todoModel: TodoListViewModel.TodoModelContent) {
            viewGroup.titleTextView.text = todoModel.todoEntity.title
            viewGroup.descTextView.text = todoModel.todoEntity.description
        }
    }

    inner class TodoHeaderViewHolder(val viewGroup: ViewGroup): TodoViewHolder<TodoListViewModel.TodoHeaderModel>(viewGroup){
        override fun bind(todoModel: TodoListViewModel.TodoHeaderModel) {
            context?.let {
                viewGroup.dayOfWeekTextView.text = WeekNumber.getWeekString(it, todoModel.dayOfWeekNumber)
            }
        }
    }

    inner class TodoAdapter(var todoList: List<TodoListViewModel.TodoModel>): RecyclerView.Adapter<TodoViewHolder<*>>(){
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TodoViewHolder<*> {
            when(p1){
                TodoListViewModel.TodoModel.TYPE_HEADER -> {
                    val viewGroup = LayoutInflater.from(p0.context).inflate(R.layout.item_todo_header, p0, false) as ViewGroup
                    return TodoHeaderViewHolder(viewGroup)
                }
                else -> {
                    val viewGroup = LayoutInflater.from(p0.context).inflate(R.layout.item_todo_content, p0, false) as ViewGroup
                    return TodoContentViewHolder(viewGroup)
                }
            }
        }

        override fun getItemCount(): Int {
            return todoList.size
        }

        override fun getItemViewType(position: Int): Int {
            return todoList[position].getType()
        }

        override fun onBindViewHolder(p0: TodoViewHolder<*>, p1: Int) {
            when(p0){
                is TodoContentViewHolder -> {
                    p0.bind(todoList[p1] as TodoListViewModel.TodoModelContent)
                }
                is TodoHeaderViewHolder -> {
                    p0.bind(todoList[p1] as TodoListViewModel.TodoHeaderModel)
                }
            }
        }
    }

    private val headerDecoration = object: HeaderDecoration() {
        override fun createHeaderViewHolder(
            context: Context,
            parent: RecyclerView,
            position: Int
        ): RecyclerView.ViewHolder {
            val viewGroup = LayoutInflater.from(context).inflate(R.layout.item_todo_header, parent, false) as ViewGroup
            return TodoHeaderViewHolder(viewGroup).apply {
                bind((parent.adapter as TodoAdapter).todoList[position] as TodoListViewModel.TodoHeaderModel)
            }
        }

        override fun isHeaderItem(recyclerView: RecyclerView, position: Int): Boolean {
            return (recyclerView.adapter as TodoAdapter).todoList[position].getType() == TodoListViewModel.TodoModel.TYPE_HEADER
        }

    }
}