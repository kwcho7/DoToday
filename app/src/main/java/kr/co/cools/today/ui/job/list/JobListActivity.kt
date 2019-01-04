package kr.co.cools.today.ui.job.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_joblist.*
import kotlinx.android.synthetic.main.item_job_content.view.*
import kotlinx.android.synthetic.main.item_job_header.view.*
import kr.co.cools.today.R
import kr.co.cools.today.di.ViewModelFactory
import kr.co.cools.today.repo.entities.JobEntity
import kr.co.cools.today.ui.utils.WeekNumber
import retrofit2.http.HEAD
import javax.inject.Inject

class JobListActivity: DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: JobListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joblist)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(JobListViewModel::class.java)
        initRecyclerView()
        initOvserver()
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
                (jobRecyclerView.adapter as JobAdapter).apply {
                    viewState.jobList.forEach {
                        itemList.add(JobItem(it))
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun initRecyclerView() {
        jobRecyclerView.apply {
            adapter = JobAdapter()
            layoutManager = LinearLayoutManager(this@JobListActivity, LinearLayoutManager.VERTICAL, false)
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

    open class BaseItem{
    }

    data class JobItem(
        val jobEntity: JobEntity
    ): BaseItem()

    data class HeaderItem(
        val dayOfWeek: Int
    ): BaseItem()
}