package kr.co.cools.today.ui.launcher

import android.content.Context
import io.reactivex.Single
import kr.co.cools.today.repo.TodayRepository
import javax.inject.Inject

class LauncherInteractor @Inject constructor(val context: Context, var repo: TodayRepository) {

    fun hasJobEntity(): Single<Boolean> {
        return repo.getJobs()
            .flatMap {
                Single.just(it.isNotEmpty())
            }

    }
}

