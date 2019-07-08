package kr.co.cools.today.ui.todo.list

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kr.co.cools.today.repo.entities.TodoEntity
import kr.co.cools.today.ui.BaseViewModel
import javax.inject.Inject

class TodoListViewModel @Inject constructor(val interactor: TodoListInteractor): BaseViewModel<TodoListViewModel.TodoListViewModelState>() {

    private val todoListLivedata = interactor.getTodoLiveData()

    override fun dispatch(action: ViewModelAction) {
    }

    override fun observer(lifecycleOwner: LifecycleOwner, observer: Observer<TodoListViewModelState>) {
        super.observer(lifecycleOwner, observer)
        todoListLivedata.observe(lifecycleOwner, Observer {
            it?.apply {
                if(it.isNotEmpty()){
                    onChangedTodoEntityList(this)
                }
                else {
                    notifyChangeViewState(TodoListViewModelState.EmptyTodoList)
                }
            }
        })
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner) {
        super.removeObservers(lifecycleOwner)
        todoListLivedata.removeObservers(lifecycleOwner)
    }

    private fun onChangedTodoEntityList(todoEntityList: List<TodoEntity>) {
        val todoModelList = mutableListOf<TodoModel>()
        var modelType = -1
        todoEntityList.forEach {
            if(it.dayOfWeekNumber != modelType){
                modelType = it.dayOfWeekNumber
                todoModelList.add(
                    TodoHeaderModel(it.dayOfWeekNumber)
                )
            }
            todoModelList.add(
                TodoModelContent(it)
            )
        }
        notifyChangeViewState(TodoListViewModelState.UpdateTodoList(todoModelList))
    }

    sealed class TodoListViewModelState: BaseViewModel.ViewModelState {
        data class UpdateTodoList(val todoListModel: List<TodoModel>): TodoListViewModelState()
        object EmptyTodoList: TodoListViewModelState()
    }

    interface TodoModel{
        fun getType(): Int

        companion object {
            val TYPE_HEADER = 0
            val TYPE_CONTENT = 1
        }
    }

    class TodoModelContent(val todoEntity: TodoEntity): TodoModel{
        override fun getType(): Int {
            return TodoModel.TYPE_CONTENT
        }
    }

    class TodoHeaderModel(val dayOfWeekNumber:Int): TodoModel {
        override fun getType(): Int {
            return TodoModel.TYPE_HEADER
        }
    }
}