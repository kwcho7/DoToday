package kr.co.cools.today.ui.job.list

import android.os.Bundle
import android.view.*
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_joblist.*
import kotlinx.android.synthetic.main.item_job_content.view.*
import kotlinx.android.synthetic.main.item_job_header.view.*
import kr.co.cools.today.R
import kr.co.cools.today.ui.todo.register.RegisterTodoActivity
import kr.co.cools.today.ui.utils.WeekNumber

@Keep
@AndroidEntryPoint
class JobListFragment: Fragment() {

    private val jobListViewModel: JobListViewModel by viewModels()
    private val jobListAdapter = JobAdapter()
    private val jobDoneListAdapter = JobAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_joblist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        jobListViewModel.observer(this, Observer { viewState ->
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
                    (activity as JobListActivity).supportActionBar?.title = viewState.dayOfWeek
                }
            }
        })
        initTabLayout()
        initRecyclerView()
        initFloatingButton()
        initToolBar()
    }


    private fun initToolBar() {
        (activity as JobListActivity).apply {
            setSupportActionBar(toolBar)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_reorder_black_24)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    }

    private fun initFloatingButton() {
        floatingActionButton.setOnClickListener {
            context?.let {
                startActivity(RegisterTodoActivity.forIntent(it, WeekNumber.getNumber()))
            }

        }
    }

    private fun initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.today_job))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.today_done_job))
        tabLayout.addOnTabSelectedListener(tabSelectListener)
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


    private fun initRecyclerView() {
        jobRecyclerView.apply {
            adapter = jobListAdapter
            layoutManager = LinearLayoutManager(
                context,
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
                        jobListViewModel.updateCompleteJob(baseItem.jobEntity)
                    }
                }
            }
        })

        helper.attachToRecyclerView(jobRecyclerView)
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


    inner class JobAdapter: RecyclerView.Adapter<AbstractViewHolder<*>>() {
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
}