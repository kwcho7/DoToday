package kr.co.cools.today.ui.todo.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_todo_list.*
import kotlinx.android.synthetic.main.item_todo_content.view.*
import kotlinx.android.synthetic.main.item_todo_header.view.*
import kr.co.cools.common.extension.gone
import kr.co.cools.common.extension.visible
import kr.co.cools.design.deco.HeaderDecoration
import kr.co.cools.today.R
import kr.co.cools.today.di.ViewModelFactory
import kr.co.cools.today.ui.todo.register.RegisterTodoActivity
import kr.co.cools.today.ui.utils.WeekNumber
import javax.inject.Inject

class TodoListActivity: DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: TodoListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)
        initViewModel()
        initRecyclerView()
        initButtonAction()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeObservers(this)
        todoRecyclerView.removeItemDecoration(headerDecoration)
    }

    // ViewModel 초기화
    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TodoListViewModel::class.java)
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
            layoutManager = LinearLayoutManager(this@TodoListActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(headerDecoration)
        }
    }

    // Button Action 초기화
    private fun initButtonAction() {
        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, RegisterTodoActivity::class.java))
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
            viewGroup.dayOfWeekTextView.text = WeekNumber.getWeekString(this@TodoListActivity, todoModel.dayOfWeekNumber)
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