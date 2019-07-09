package kr.co.cools.today.ui.job.list

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchUIUtil
import android.view.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_joblist.*
import kotlinx.android.synthetic.main.item_job_content.view.*
import kotlinx.android.synthetic.main.item_job_header.view.*
import kr.co.cools.common.logger.Logger
import kr.co.cools.today.R
import kr.co.cools.today.di.ViewModelFactory
import kr.co.cools.today.repo.entities.JobEntity
import kr.co.cools.today.ui.todo.list.TodoListActivity
import kr.co.cools.today.ui.todo.register.RegisterTodoActivity
import kr.co.cools.today.ui.utils.WeekNumber
import javax.inject.Inject

class JobListActivity: DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: JobListViewModel

    private val jobListAdapter = JobAdapter()
    private val jobDoneListAdapter = JobAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joblist)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(JobListViewModel::class.java)
        initToolBar()
        initRecyclerView()
        initOvserver()
        initTabLayout()
        initFloatingButton()
    }

    private fun initFloatingButton() {
        floatingActionButton.setOnClickListener {
            startActivity(RegisterTodoActivity.forIntent(this, WeekNumber.getNumber()))
        }
    }

    private fun initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.today_job))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.today_done_job))
        tabLayout.addOnTabSelectedListener(tabSelectListener)
    }

    private fun initToolBar() {
        setSupportActionBar(toolBar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_reorder_black_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nav_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.setting -> {
                    startActivity(Intent(this@JobListActivity, TodoListActivity::class.java))
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        if(drawableLayout.isDrawerOpen(Gravity.LEFT)){
            drawableLayout.closeDrawer(Gravity.LEFT)
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayout.removeOnTabSelectedListener(tabSelectListener)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                drawableLayout.openDrawer(Gravity.LEFT)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initOvserver() {
        viewModel.observer(this, Observer {
            it?.let {
                onChangeViewState(it)
            }
        })
    }

    private fun onChangeViewState(viewState: JobListViewModel.JobListViewState) {
        when(viewState){
            is JobListViewModel.JobListViewState.UpdateJobList -> {
                jobListAdapter.apply {
                    itemList.clear()
                    viewState.jobList.forEach {
                        itemList.add(JobItem(it))
                    }
                    notifyDataSetChanged()
                }
            }
            is JobListViewModel.JobListViewState.UpdateDoneJobList -> {
                jobDoneListAdapter.apply {
                    itemList.clear()
                    viewState.jobList.forEach {
                        itemList.add(JobItem(it))
                    }
                    notifyDataSetChanged()
                }
            }

            is JobListViewModel.JobListViewState.UpdateDayOfWeek -> {
                supportActionBar?.title = viewState.dayOfWeek
            }
        }
    }

    private fun initRecyclerView() {
        jobRecyclerView.apply {
            adapter = jobListAdapter
            layoutManager = LinearLayoutManager(
                this@JobListActivity,
                RecyclerView.VERTICAL,
                false
            )
        }

        val helper = ItemTouchHelper(object: ItemTouchHelper.Callback(){
            override fun getMovementFlags(p0: RecyclerView, p1: RecyclerView.ViewHolder): Int {
                if(jobRecyclerView.adapter == jobListAdapter){
                    return makeMovementFlags(0, ItemTouchHelper.RIGHT)
                }
                return makeMovementFlags(0, 0)
            }

            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
                if(jobRecyclerView.adapter == jobListAdapter){
                    val baseItem = (jobRecyclerView.adapter as JobAdapter).itemList.get(p0.adapterPosition)
                    if(baseItem is JobItem){
                        viewModel.updateCompleteJob(baseItem.jobEntity)
                    }
                }
            }
        })

        helper.attachToRecyclerView(jobRecyclerView)
    }

    private val tabSelectListener = object: TabLayout.OnTabSelectedListener{
        override fun onTabReselected(p0: TabLayout.Tab?) {
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
        }

        override fun onTabSelected(p0: TabLayout.Tab?) {
            p0?.let {
                if(it.position == 0){
                    floatingActionButton.show()
                    jobRecyclerView.adapter = jobListAdapter
                    jobRecyclerView.adapter?.notifyDataSetChanged()
                }
                else {
                    floatingActionButton.hide()
                    jobRecyclerView.adapter = jobDoneListAdapter
                    jobRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }

    }

    abstract class AbstractViewHolder<T>(val viewGroup: ViewGroup): RecyclerView.ViewHolder(viewGroup) {
        abstract fun bind(item: T)
    }

    class JobViewHolder(val rootView: ViewGroup): AbstractViewHolder<JobItem>(rootView) {
        override fun bind(item: JobItem) {
            rootView.titleTextView.text = item.jobEntity.todo?.title
            rootView.descTextView.text = item.jobEntity.todo?.description
        }
    }

    class JobHeaderViewHolder(val rootView: ViewGroup): AbstractViewHolder<HeaderItem>(rootView) {
        override fun bind(item: HeaderItem) {
            rootView.dayOfWeekTextView.text = WeekNumber.getWeekString(rootView.context, item.dayOfWeek)
        }
    }


    inner class JobAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<AbstractViewHolder<*>>() {
        val TYPE_HEADER = 0
        val TYPE_JOB = 1
        var itemList = mutableListOf<BaseItem>()

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AbstractViewHolder<*> {
            if(p1 == TYPE_HEADER){
                val view = LayoutInflater.from(p0.context).inflate(R.layout.item_job_header, p0, false) as ViewGroup
                return JobHeaderViewHolder(view)
            }else {
                val view = LayoutInflater.from(p0.context).inflate(R.layout.item_job_content, p0, false) as ViewGroup
                return JobViewHolder(view)
            }
        }

        override fun getItemViewType(position: Int): Int {
            return if(itemList[position] is HeaderItem) TYPE_HEADER else TYPE_JOB
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        override fun onBindViewHolder(p0: AbstractViewHolder<*>, p1: Int) {
            when(p0){
                is JobViewHolder -> {
                    p0.bind(itemList[p1] as JobItem)
                }
                is JobHeaderViewHolder -> {
                    p0.bind(itemList[p1] as HeaderItem)
                }
            }
        }
    }

    open class BaseItem{
    }

    data class JobItem(
        val jobEntity: JobEntity
    ): BaseItem()

    data class HeaderItem(
        val dayOfWeek: Int
    ): BaseItem()
}