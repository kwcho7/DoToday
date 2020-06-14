package kr.co.cools.today.ui.job.list

import kr.co.cools.today.repo.entities.JobEntity

data class JobItem(
    val jobEntity: JobEntity
): BaseItem()