package com.studio.hello.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.studio.hello.domain.repository.ScheduleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class MidnightCleanupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: ScheduleRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            repository.deleteOldSchedules()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "MidnightCleanupWorker"

        fun enqueue(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiresDeviceIdle(false)
                .build()

            val cleanupRequest = PeriodicWorkRequestBuilder<MidnightCleanupWorker>(
                24, TimeUnit.HOURS
            ).setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                cleanupRequest
            )
        }
    }
}
